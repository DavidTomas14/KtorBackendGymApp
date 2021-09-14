package com.dtalonso.data.di

import com.dtalonso.controller.user.UserController
import com.dtalonso.controller.user.UserControllerImpl
import com.dtalonso.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }

    single <UserController> {
        UserControllerImpl(get())
    }
}