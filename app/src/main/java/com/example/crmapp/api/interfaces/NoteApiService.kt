package com.example.crmapp.api.interfaces

import com.example.crmapp.api.requests.NoteRequest
import com.example.crmapp.api.responses.NoteResponse
import com.example.crmapp.api.responses.StatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApiService {
    @GET("users/{userId}/contacts/{contactId}/notes")
    suspend fun getNotesForContact(
        @Path("userId") userId: String,
        @Path("contactId") contactId: Int
    ): Response<List<NoteResponse>>

    @GET("users/{userId}/contacts/notes/{noteId})")
    suspend fun getNoteById(
        @Path("userId") userId: String,
        @Path("noteId") noteId: Int
    ): Response<NoteResponse>

    @POST("users/{userId}/contacts/notes")
    suspend fun createNote(
        @Path("userId") userId: String,
        @Body request: NoteRequest
    ): Response<NoteResponse>

    @PUT("users/{userId}/contacts/notes/{noteId}")
    suspend fun editNote(
        @Path("userId") userId: String,
        @Path("noteId") noteId: Int,
        @Body request: NoteRequest
    ): Response<StatusResponse>

    @DELETE("users/{userId}/contacts/notes/{noteId}")
    suspend fun deleteNote(
        @Path("userId") userId: String,
        @Path("noteId") noteId: Int,
    ): Response<StatusResponse>
}