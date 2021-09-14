package com.dtalonso

import com.dtalonso.data.di.mainModule
import io.ktor.application.*
import com.dtalonso.plugins.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.modules

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSockets()
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    install(Koin) {
        modules(mainModule)
    }
}
