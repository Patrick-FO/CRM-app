package com.example.crmapp.api.requests

data class NoteRequest(
    val contactIds: List<Int>,
    val title: String,
    val description: String
)