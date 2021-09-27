package com.dtalonso.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dtalonso.data.repository.user.UserRepository
import com.dtalonso.data.models.User
import com.dtalonso.data.requests.AccountAuthRequest
import com.dtalonso.data.responses.AuthResponse
import com.dtalonso.data.responses.BasicApiResponse
import com.dtalonso.data.service.UserService
import com.dtalonso.data.service.UserService.*
import com.dtalonso.routes.authenticate
import com.dtalonso.util.ApiResponseMessages.FIELDS_BLANK
import com.dtalonso.util.ApiResponseMessages.PASSWORD_DOESNT_MATCH
import com.dtalonso.util.ApiResponseMessages.SUCCESSFULLY_LOGGED_IN
import com.dtalonso.util.ApiResponseMessages.USER_ALREADY_EXIST
import com.dtalonso.util.ApiResponseMessages.USER_DOESNT_EXIST
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.createUser(userService: UserService) {
    post("/api/user/create") {
        val request = call.receiveOrNull<AccountAuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (userService.doesUserExist(request.username)) {
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = USER_ALREADY_EXIST
                )
            )
            return@post
        }
        when (userService.validateAccountRequest(request)) {
            is ValidationEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
                return@post
            }
            is ValidationEvent.Success -> {
                userService.createUser(request)
                call.respond(
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            }
        }
    }
}


fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveOrNull<AccountAuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (request.username.isBlank() || request.password.isBlank()) {
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = FIELDS_BLANK
                )
            )
            return@post
        }
        val user = userService.getUserByUsername(request.username)
        if (user == null) {
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = USER_DOESNT_EXIST
                )
            )
            return@post
        }

        val isCorrectPassword = userService.doesPasswordMatchForUser(request)
        if (isCorrectPassword) {
            val expiresIn = 1000L * 60L * 60L * 365L
            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = AuthResponse(token = token)
                )

            )
        } else {
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = PASSWORD_DOESNT_MATCH
                )
            )
        }
    }
}

fun Route.authenticate() {
    authenticate {
        get("/api/user/authenticate") {
            call.respond(
                HttpStatusCode.OK
            )
        }
    }

}
