package com.example.crmapp.domain.model.entities

import java.util.UUID

data class UserEntity(
    val id: String,
    val username: String,
    val password: String
)
