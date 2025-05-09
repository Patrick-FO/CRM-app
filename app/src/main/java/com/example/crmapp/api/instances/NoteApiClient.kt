package com.example.crmapp.api.instances

import com.example.crmapp.api.interfaces.NoteApiService
import com.example.crmapp.api.interfaces.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*object NoteApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val noteApiService: NoteApiService = retrofit.create(NoteApiService::class.java)
}*/