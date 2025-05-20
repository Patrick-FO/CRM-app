package com.example.crmapp.domain.usecase.impl

import android.util.Log
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

        //TODO Consider adding safety in case user ID is null, like throwing an error or not returning a successful result, like throwing an error altogether on this function if user ID is null, maybe getOrThrow()?
        val result = userRepository.loginUser(username, password)
        val userId = getUserId(username, password).getOrNull()
        appState.setAuthState()
        appState.setUserId(userId)
        return result
    }

    override suspend fun getUserId(username: String, password: String): Result<String> {
        if(username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Both a username and password are required"))
        }

        val result = userRepository.getUserId(username, password)

        if(result.isSuccess) {
            val userId = result.getOrNull()
            if(userId != null) {
                appState.setUserId(userId)
            }
        } else {
            println("Failed to get user ID: ${result.exceptionOrNull()?.message}")
        }

        return result
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
            appState.setUserId(null)
            Result.success(true)
        } catch(e: Exception) {
            Result.failure(e)
        }
    }
}