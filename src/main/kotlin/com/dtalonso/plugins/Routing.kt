package com.dtalonso.plugins

import com.dtalonso.routes.userRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        userRoutes()
    }
}
