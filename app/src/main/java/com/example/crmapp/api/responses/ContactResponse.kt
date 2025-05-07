package com.example.crmapp.api.responses

import java.util.UUID

data class ContactResponse(
    val id: Int,
    val userId: String,
    val name: String,
    val company: String?,
    val phoneNumber: String?,
    val contactEmail: String?
)