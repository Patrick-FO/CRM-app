package com.example.crmapp.di

import com.example.crmapp.data.security.KeystoreManager
import com.example.crmapp.data.storage.JwtStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val securityModule = module {
    single { KeystoreManager() }
    single { JwtStorage(androidContext()) }
}