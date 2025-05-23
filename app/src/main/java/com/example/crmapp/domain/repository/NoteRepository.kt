package com.example.crmapp.domain.repository

import com.example.crmapp.domain.model.entities.NoteEntity

interface NoteRepository {
    suspend fun getNotesForContact(userId: String, contactId: Int): Result<List<NoteEntity>>

    suspend fun getNoteById(userId: String, noteId: Int): Result<NoteEntity>

    suspend fun createNote(userId: String, contactIds: List<Int>, title: String, description: String): Result<NoteEntity>

    suspend fun editNote(userId: String, noteId: Int, contactIds: List<Int>, title: String, description: String?): Result<Boolean>

    suspend fun deleteNote(userId: String, noteId: Int): Result<Boolean>
}