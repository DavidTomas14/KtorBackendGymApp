package com.dtalonso.data.service

import com.dtalonso.data.models.User
import com.dtalonso.data.repository.user.UserRepository
import com.dtalonso.data.requests.AccountAuthRequest

class UserService (
    private val userRepository: UserRepository
    ){

    suspend fun doesUserExist(userName: String) : Boolean {
        return userRepository.getUserByUsername(userName) != null
    }

    suspend fun doesUserNameBelongToUserId(userName: String, userId: String): Boolean {
        return userRepository.doesUserNameBelongToUserId(userName, userId)
    }

    suspend fun getUserByUsername(userName: String): User? {
        return userRepository.getUserByUsername(userName)
    }


    fun validateAccountRequest(request: AccountAuthRequest): ValidationEvent {
        if (request.username.isBlank() || request.password.isBlank()){
            return ValidationEvent.ErrorFieldEmpty
        }else{
            return ValidationEvent.Success
        }
    }

    suspend fun createUser(request: AccountAuthRequest) {
        userRepository.createUser(
            User(
                username = request.username,
                password = request.password
            )
        )
    }

    suspend fun doesPasswordMatchForUser(request: AccountAuthRequest): Boolean{
        return userRepository.doesPasswordForUserMatch(
            request.username,
            request.password
        )
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty: ValidationEvent()
        object Success: ValidationEvent()
    }
}