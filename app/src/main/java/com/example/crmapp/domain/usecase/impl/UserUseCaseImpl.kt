package com.example.crmapp.domain.usecase.impl

import com.example.crmapp.domain.model.entities.JwtEntity
import com.example.crmapp.domain.repository.UserRepository
import com.example.crmapp.domain.usecase.interfaces.UserUseCase

class UserUseCaseImpl(
    private val userRepository: UserRepository
): UserUseCase {
    override suspend fun createUser(username: String, password: String): Result<Boolean> {
        if(username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }

        if(password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters long"))
        }

        return userRepository.createUser(username, password)
    }

    override suspend fun loginUser(username: String, password: String): Result<JwtEntity> {
        if(username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Both a username and password are required"))
        }

        return userRepository.loginUser(username, password)
    }
}