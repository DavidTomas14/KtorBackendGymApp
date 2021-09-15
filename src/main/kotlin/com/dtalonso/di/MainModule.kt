package com.dtalonso.di

import com.dtalonso.data.repository.user.UserRepository
import com.dtalonso.data.repository.user.UserRepositoryImpl
import com.dtalonso.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }

    single <UserRepository> {
        UserRepositoryImpl(get())
    }
}