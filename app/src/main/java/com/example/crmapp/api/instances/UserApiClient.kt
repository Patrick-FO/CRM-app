package com.example.crmapp.api.instances

import com.example.crmapp.api.interfaces.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object UserApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApiService: UserApiService = retrofit.create(UserApiService::class.java)
}