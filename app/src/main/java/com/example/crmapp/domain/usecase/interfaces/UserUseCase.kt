package com.example.crmapp.domain.usecase.interfaces

interface UserUseCase {
    suspend fun createUser(username: String, password: String): Result<Boolean>
    suspend fun loginUser(username: String, password: String): Result<String>
    suspend fun getUserId(username: String, password: String): Result<String>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getStoredJwt(): String?
    suspend fun logoutUser(): Result<Boolean>
}