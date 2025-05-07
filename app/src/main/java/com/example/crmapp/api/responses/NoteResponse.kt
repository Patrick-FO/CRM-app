package com.example.crmapp.api.responses

import java.util.UUID

data class NoteResponse(
    val id: Int,
    val userId: String,
    val contactIds: List<Int>,
    val title: String,
    val description: String?
)