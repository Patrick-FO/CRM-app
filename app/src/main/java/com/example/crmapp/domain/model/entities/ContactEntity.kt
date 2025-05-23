package com.example.crmapp.domain.model.entities

data class ContactEntity(
    val id: Int,
    val userId: String,
    val name: String,
    val company: String?,
    val phoneNumber: String?,
    val contactEmail: String?,
)
