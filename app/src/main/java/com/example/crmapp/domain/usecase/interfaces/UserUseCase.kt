package com.example.crmapp.domain.usecase.interfaces

import com.example.crmapp.domain.model.entities.JwtEntity

interface UserUseCase {
    suspend fun createUser(username: String, password: String): Result<Boolean>

    suspend fun loginUser(username: String, password: String): Result<JwtEntity>
}