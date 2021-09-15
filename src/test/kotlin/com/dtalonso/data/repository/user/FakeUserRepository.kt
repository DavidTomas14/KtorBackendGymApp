package com.dtalonso.data.repository.user

import com.dtalonso.data.models.User

class FakeUserRepository: UserRepository {

    val users = mutableListOf<User>()

    override suspend fun createUser(user: User) {
        users.add(user)
    }

    override suspend fun getUserId(id: String): User? {
        return users.find{ it.id == id }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return users.find { it.username == username }
    }

    override suspend fun doesPasswordForUserMatch(username: String, enteredPassword: String): Boolean {
        val user = getUserByUsername(username)
        return user?.password == enteredPassword
    }
}