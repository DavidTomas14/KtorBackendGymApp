package com.dtalonso.data.repository.user

import com.dtalonso.data.models.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserId(id: String): User?

    suspend fun getUserByUsername(username: String): User?

    suspend fun doesPasswordForUserMatch(username: String, enteredPassword: String): Boolean
}