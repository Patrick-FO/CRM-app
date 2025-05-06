package com.example.crmapp.api.instances

import com.example.crmapp.api.interfaces.ContactApiService
import com.example.crmapp.api.interfaces.UserApiService
import com.example.crmapp.api.requests.ContactRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ContactApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val contactApiService: ContactApiService = retrofit.create(ContactApiService::class.java)
}