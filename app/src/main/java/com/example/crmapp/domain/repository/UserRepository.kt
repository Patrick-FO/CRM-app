package com.example.crmapp.domain.repository

import com.example.crmapp.domain.model.entities.JwtEntity

interface UserRepository {
    suspend fun createUser(username: String, password: String): Result<String>

    suspend fun loginUser(username: String, password: String): Result<JwtEntity>
}