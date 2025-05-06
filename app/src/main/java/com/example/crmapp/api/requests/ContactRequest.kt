package com.example.crmapp.api.requests

data class ContactRequest(
    val name: String,
    val company: String? = null,
    val phoneNumber: String? = null,
    val contactEmail: String? = null
)