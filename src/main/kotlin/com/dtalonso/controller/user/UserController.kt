package com.dtalonso.controller.user

import com.dtalonso.data.models.User

interface UserController {

    suspend fun createUser(user: User)

    suspend fun getUserId(id: String): User?

    suspend fun getUserByUsername(username: String): User?
}