package com.dtalonso.di

import com.dtalonso.data.repository.user.*
import com.dtalonso.data.service.ExerciseService
import com.dtalonso.data.service.UserService
import com.dtalonso.data.service.WeightsService
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
    single <ExerciseRepository> {
        ExerciseRepositoryImpl(get())
    }
    single <WeightsRepository> {
        WeightsRepositoryImpl(get())
    }
    single {UserService(get())}
    single {ExerciseService(get())}
    single {WeightsService(get())}
}