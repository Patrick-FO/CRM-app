package com.example.crmapp.di.modules

import com.example.crmapp.api.interceptor.AuthInterceptor
import com.example.crmapp.api.interfaces.ContactApiService
import com.example.crmapp.api.interfaces.NoteApiService
import com.example.crmapp.api.interfaces.UserApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { AuthInterceptor(get()) }

    single<Gson> {
        GsonBuilder().create()
    }

    //What is this OkHttp client doing? Is it just for debugging?
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(get<AuthInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") //Consider making this configurable, but why?
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<UserApiService> {
        get<Retrofit>().create(UserApiService::class.java)
    }

    single<ContactApiService> {
        get<Retrofit>().create(ContactApiService::class.java)
    }

    single<NoteApiService> {
        get<Retrofit>().create(NoteApiService::class.java)
    }
}