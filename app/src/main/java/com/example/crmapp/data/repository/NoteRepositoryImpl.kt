package com.example.crmapp.data.repository

import com.example.crmapp.api.interfaces.NoteApiService
import com.example.crmapp.domain.model.entities.NoteEntity
import com.example.crmapp.domain.repository.NoteRepository

class NoteRepositoryImpl(noteApiService: NoteApiService): NoteRepository {
    override suspend fun getNotesForContact(
        userId: String,
        contactId: Int
    ): Result<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteById(userId: String, noteId: Int): Result<NoteEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun createNote(
        userId: String,
        contactIds: List<Int>,
        title: String,
        description: String
    ): Result<NoteEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun editNote(
        userId: String,
        noteId: Int,
        contactIds: List<Int>,
        title: String,
        description: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(userId: String, noteId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}