package com.dtalonso.plugins

import com.dtalonso.data.repository.user.UserRepository
import com.dtalonso.routes.createUserRoute
import com.dtalonso.routes.loginUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userRepository: UserRepository by inject()

    routing {
        createUserRoute(userRepository)
        loginUser(userRepository)
    }
}
