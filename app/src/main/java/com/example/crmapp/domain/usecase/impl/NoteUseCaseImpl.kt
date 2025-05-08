package com.example.crmapp.domain.usecase.impl

import com.example.crmapp.domain.model.entities.NoteEntity
import com.example.crmapp.domain.repository.NoteRepository
import com.example.crmapp.domain.usecase.interfaces.NoteUseCase

class NoteUseCaseImpl(
    private val noteRepository: NoteRepository
): NoteUseCase {
    override suspend fun getNotesForContact(
        userId: String,
        contactId: Int
    ): Result<List<NoteEntity>> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(contactId <= 0) {
            return Result.failure(IllegalArgumentException("Contact ID must be 1 or greater"))
        }

        return noteRepository.getNotesForContact(userId, contactId)
    }

    override suspend fun getNoteById(userId: String, noteId: Int): Result<NoteEntity> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(noteId <= 0) {
            return Result.failure(IllegalArgumentException("Note ID must be 1 or greater"))
        }

        return noteRepository.getNoteById(userId, noteId)
    }

    override suspend fun createNote(
        userId: String,
        contactIds: List<Int>,
        title: String,
        description: String
    ): Result<NoteEntity> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(contactIds.isEmpty()) {
            return Result.failure(IllegalArgumentException("Note must be associated with one or more contacts"))
        }

        if(title.isBlank()) {
            return Result.failure(IllegalArgumentException("Title must not be blank"))
        }

        if(description.isBlank()) {
            return Result.failure(IllegalArgumentException("Description must not be blank"))
        }

        return noteRepository.createNote(userId, contactIds, title, description)
    }

    override suspend fun editNote(
        userId: String,
        noteId: Int,
        contactIds: List<Int>,
        title: String,
        description: String
    ): Result<Boolean> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(noteId <= 0) {
            return Result.failure(IllegalArgumentException("Note ID must be 1 or greater"))
        }

        if(contactIds.isEmpty()) {
            return Result.failure(IllegalArgumentException("Note must be associated with one or more contacts"))
        }

        if(title.isBlank()) {
            return Result.failure(IllegalArgumentException("Title must not be blank"))
        }

        if(description.isBlank()) {
            return Result.failure(IllegalArgumentException("Description must not be blank"))
        }

        return noteRepository.editNote(userId, noteId, contactIds, title, description)
    }

    override suspend fun deleteNote(userId: String, noteId: Int): Result<Boolean> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(noteId <= 0) {
            return Result.failure(IllegalArgumentException("Note ID must be 1 or greater"))
        }

        return noteRepository.deleteNote(userId, noteId)
    }
}