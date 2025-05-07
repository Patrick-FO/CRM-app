package com.example.crmapp.api.interfaces

import com.example.crmapp.api.requests.ContactRequest
import com.example.crmapp.api.responses.ContactResponse
import com.example.crmapp.api.responses.StatusResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ContactApiService {
    @GET("users/{userId}/contacts")
    suspend fun getAllContacts(@Path("userId") userId: String): Response<List<ContactResponse>>

    @GET("users/{userId}/contacts/{contactId}")
    suspend fun getContactById(
        @Path("userId") userId: String,
        @Path("contactId") contactId: Int
    ): Response<ContactResponse>

    @POST("users/{userId}/contacts")
    suspend fun createContact(
        @Path("userId") userId: String,
        @Body request: ContactRequest
    ): Response<ContactResponse>

    @PUT("users/{userId}/contacts/{contactId}")
    suspend fun editContact(
        @Path("userId") userId: String,
        @Path("contactId") contactId: Int,
        @Body request: ContactRequest
    ): Response<StatusResponse>

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Path("userId") userId: String,
        @Path("contactId") contactId: Int,
    ): Response<StatusResponse>
}