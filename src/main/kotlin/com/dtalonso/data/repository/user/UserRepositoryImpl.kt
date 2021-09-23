package com.dtalonso.data.repository.user

import com.dtalonso.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserRepositoryImpl(
   db: CoroutineDatabase
): UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun createUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun getUserId(id: String): User? {
        return users.findOneById(id)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun doesPasswordForUserMatch(username: String, enteredPassword: String): Boolean {
        val user = getUserByUsername(username)
        return user?.password == enteredPassword
    }

    override suspend fun doesUserNameBelongToUserId(username: String, userId: String): Boolean {
        return users.findOneById(userId)?.username == username
    }

}