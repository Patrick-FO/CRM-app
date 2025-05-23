package com.example.crmapp.domain.model.entities

data class NoteEntity(
    val id: Int,
    val userId: String,
    val contactIds: List<Int>,
    val title: String,
    val description: String?
)
