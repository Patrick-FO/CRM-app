package com.example.crmapp.di

import com.example.crmapp.api.interceptor.AuthInterceptor
import com.example.crmapp.data.storage.JwtStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    //single { SessionManager(get()) }
    //single { AppConfig() }
}