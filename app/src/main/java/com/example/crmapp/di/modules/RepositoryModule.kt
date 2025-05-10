package com.example.crmapp.di.modules

import com.example.crmapp.data.repository.ContactRepositoryImpl
import com.example.crmapp.data.repository.NoteRepositoryImpl
import com.example.crmapp.data.repository.UserRepositoryImpl
import com.example.crmapp.domain.repository.ContactRepository
import com.example.crmapp.domain.repository.NoteRepository
import com.example.crmapp.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ContactRepository> { ContactRepositoryImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}