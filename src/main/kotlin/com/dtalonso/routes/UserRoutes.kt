package com.dtalonso.routes

import com.dtalonso.data.repository.user.UserRepository
import com.dtalonso.data.models.User
import com.dtalonso.data.requests.AccountAuthRequest
import com.dtalonso.data.responses.BasicApiResponse
import com.dtalonso.util.ApiResponseMessages.FIELDS_BLANK
import com.dtalonso.util.ApiResponseMessages.PASSWORD_DOESNT_MATCH
import com.dtalonso.util.ApiResponseMessages.SUCCESSFULLY_LOGGED_IN
import com.dtalonso.util.ApiResponseMessages.USER_ALREADY_EXIST
import com.dtalonso.util.ApiResponseMessages.USER_DOESNT_EXIST
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUserRoute(userRepository: UserRepository) {
    post("/api/user/create") {
        val request = call.receiveOrNull<AccountAuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val userExist = userRepository.getUserByUsername(request.username) != null
        if (userExist) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = USER_ALREADY_EXIST
                )
            )
            return@post
        }
        if (request.username.isBlank() || request.password.isBlank()) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = FIELDS_BLANK
                )
            )
            return@post
        }
        userRepository.createUser(
            User(
                username = request.username,
                password = request.password
            )
        )
        call.respond(
            BasicApiResponse(
                successful = true,
                message = null
            )
        )
    }
}


fun Route.loginUser(userRepository: UserRepository) {
    post("/api/user/login") {
        val request = call.receiveOrNull<AccountAuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (request.username.isBlank() || request.password.isBlank()) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = FIELDS_BLANK
                )
            )
            return@post
        }
        val userDoesntExists = userRepository.getUserByUsername(request.username) == null
        if (userDoesntExists) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = USER_DOESNT_EXIST
                )
            )
            return@post
        }
        val passwordMatches = userRepository.doesPasswordForUserMatch(request.username, request.password)
        if (!passwordMatches) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = PASSWORD_DOESNT_MATCH
                )
            )
        } else {
            call.respond(
                BasicApiResponse(
                    successful = true,
                    message = SUCCESSFULLY_LOGGED_IN
                )
            )
        }
    }
}
