package com.example.crmapp.api.interfaces

import com.example.crmapp.api.requests.ContactRequest
import com.example.crmapp.api.requests.NoteRequest
import com.example.crmapp.api.responses.ContactResponse
import com.example.crmapp.api.responses.NoteResponse
import com.example.crmapp.api.responses.StatusResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface NoteApiService {
    @GET("users/{userId}/contacts/{contactId}/notes")
    suspend fun getNotesForContact(
        @Path("userId") userId: String,
        @Path("contactId") contactId: Int
    ): List<NoteResponse>

    @GET("users/{userId}/contacts/notes/{noteId})")
    suspend fun getNoteById(
        @Path("userId") userId: String,
        @Path("noteId") noteId: Int
    ): NoteResponse

    @POST("users/{userId}/contacts/notes")
    suspend fun createNote(
        @Path("userId") userId: String,
        @Body request: NoteRequest
    ): NoteResponse

    @PUT("users/{userId}/contacts/notes/{noteId}")
    suspend fun editNote(
        @Path("userId") userId: String,
        @Path("noteId") noteId: Int,
        @Body request: NoteRequest
    ): StatusResponse

    @DELETE("users/{userId}/contacts/notes/{noteId}")
    suspend fun deleteNote(
        @Path("userId") userId: String,
        @Path("noteId") noteId: Int,
    ): StatusResponse
}