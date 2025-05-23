package com.example.crmapp.data.repository

import com.example.crmapp.api.interfaces.NoteApiService
import com.example.crmapp.api.requests.NoteRequest
import com.example.crmapp.domain.model.entities.NoteEntity
import com.example.crmapp.domain.model.toDomain
import com.example.crmapp.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(private val noteApiService: NoteApiService): NoteRepository {
    override suspend fun getNotesForContact(
        userId: String,
        contactId: Int
    ): Result<List<NoteEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val response = noteApiService.getNotesForContact(userId, contactId)

                if(response.isSuccessful) {
                    val notes = response.body()
                        ?: return@withContext Result.failure(Exception("Empty notes response"))
                    val notesEntity = notes.map { it.toDomain() }

                    Result.success(notesEntity)
                } else {
                    Result.failure(Exception("Failed to get notes for contact ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }

    override suspend fun getNoteById(userId: String, noteId: Int): Result<NoteEntity> =
        withContext(Dispatchers.IO) {
            try {
                val response = noteApiService.getNoteById(userId, noteId)

                if(response.isSuccessful) {
                    val note = response.body()
                        ?: return@withContext Result.failure(Exception("Empty note response"))
                    val noteEntity = note.toDomain()

                    Result.success(noteEntity)
                } else {
                    Result.failure(Exception("Couldn't get note ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }

    override suspend fun createNote(
        userId: String,
        contactIds: List<Int>,
        title: String,
        description: String
    ): Result<NoteEntity> =
        withContext(Dispatchers.IO) {
            try {
                val request = NoteRequest(
                    contactIds = contactIds,
                    title = title,
                    description = description
                )
                val response = noteApiService.createNote(userId, request)

                if(response.isSuccessful) {
                    val note = response.body()
                        ?: return@withContext Result.failure(Exception("Note empty"))

                    val noteEntity = note.toDomain()

                    Result.success(noteEntity)
                } else {
                    Result.failure(Exception("Couldn't create note ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }

    override suspend fun editNote(
        userId: String,
        noteId: Int,
        contactIds: List<Int>,
        title: String,
        description: String?
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val request = NoteRequest(
                    contactIds = contactIds,
                    title = title,
                    description = description
                )
                val response = noteApiService.editNote(userId, noteId, request)

                if(response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to edit note: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }

    override suspend fun deleteNote(userId: String, noteId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val response = noteApiService.deleteNote(userId, noteId)

                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to delete note ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }
}