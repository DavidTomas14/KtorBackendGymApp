package com.dtalonso.routes

import com.dtalonso.controller.user.UserController
import com.dtalonso.data.models.User
import com.dtalonso.data.requests.CreateAccountRequest
import com.dtalonso.data.responses.BasicApiResponse
import com.dtalonso.util.ApiResponseMessages
import com.dtalonso.util.ApiResponseMessages.FIELDS_BLANK
import com.dtalonso.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userController: UserController by inject()
    route("/api/user/create") {
        post {
            val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userExist = userController.getUserByUsername(request.username) != null
            if (userExist) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = USER_ALREADY_EXISTS
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
            userController.createUser(User(
                username = request.username,
                password = request.password
            ))
            call.respond(
                BasicApiResponse(
                    successful = true,
                    message = null
                )
            )
        }
    }
}