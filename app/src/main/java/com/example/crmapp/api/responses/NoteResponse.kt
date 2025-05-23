package com.example.crmapp.api.responses

data class NoteResponse(
    val id: Int,
    val userId: String,
    val contactIds: List<Int>,
    val title: String,
    val description: String?
)