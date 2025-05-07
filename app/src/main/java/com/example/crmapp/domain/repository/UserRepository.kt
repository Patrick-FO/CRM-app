package com.example.crmapp.domain.repository

import java.util.UUID

interface UserRepository {
    suspend fun createUser(username: String, password: String): Result<Boolean>

    suspend fun loginUser(username: String, password: String): Result<String>
}