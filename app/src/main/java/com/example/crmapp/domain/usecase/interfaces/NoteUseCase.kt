package com.example.crmapp.domain.usecase.interfaces

import com.example.crmapp.domain.model.entities.NoteEntity

interface NoteUseCase {
    //TODO Might not be needed, as we will have another usecase that both gets the contact and notes
    suspend fun getNotesForContact(userId: String, contactId: Int): Result<List<NoteEntity>>

    //TODO Will probably not be needed
    suspend fun getNoteById(userId: String, noteId: Int): Result<NoteEntity>

    suspend fun createNote(userId: String, contactIds: List<Int>, title: String, description: String): Result<NoteEntity>

    suspend fun editNote(userId: String, noteId: Int, contactIds: List<Int>, title: String, description: String): Result<Boolean>

    suspend fun deleteNote(userId: String, noteId: Int): Result<Boolean>
}