package com.example.crmapp.domain.usecase.impl

import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.repository.UserRepository
import com.example.crmapp.domain.usecase.interfaces.UserUseCase

class UserUseCaseImpl(
    private val userRepository: UserRepository,
    private val appState: AppState
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

    override suspend fun loginUser(username: String, password: String): Result<String> {
        if(username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Both a username and password are required"))
        }

        val result = userRepository.loginUser(username, password)
        appState.setAuthState()
        return result
    }

    override suspend fun getUserId(username: String, password: String): Result<String> {
        if(username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Both a username and password are required"))
        }

        return userRepository.getUserId(username, password)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    override suspend fun getStoredJwt(): String? {
        return userRepository.getStoredJwt()
    }

    override suspend fun logoutUser(): Result<Boolean> {
        return try {
            userRepository.logoutUser()
            appState.setAuthState()
            Result.success(true)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }
}