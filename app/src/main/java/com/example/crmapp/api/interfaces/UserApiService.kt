package com.example.crmapp.api.interfaces

import com.example.crmapp.api.responses.JwtResponse
import com.example.crmapp.api.requests.UserRequest
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("user")
    suspend fun createUser(@Body request: UserRequest): Response<Unit>

    @POST("auth")
    suspend fun getJwtToken(@Body request: UserRequest): Response<JwtResponse>
}