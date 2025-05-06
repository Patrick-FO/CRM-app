package com.example.crmapp.api.interfaces

import com.example.crmapp.api.responses.JwtResponse
import com.example.crmapp.api.requests.UserRequest
import com.example.crmapp.api.responses.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("user")
    suspend fun createUser(@Body request: UserRequest): Call<UserResponse>

    @POST("auth")
    suspend fun getJwtToken(@Body request: UserRequest): Call<JwtResponse>
}