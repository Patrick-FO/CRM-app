package com.example.crmapp.di

import com.example.crmapp.domain.usecase.impl.ContactUseCaseImpl
import com.example.crmapp.domain.usecase.impl.NoteUseCaseImpl
import com.example.crmapp.domain.usecase.impl.UserUseCaseImpl
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import com.example.crmapp.domain.usecase.interfaces.NoteUseCase
import com.example.crmapp.domain.usecase.interfaces.UserUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> { UserUseCaseImpl(get()) }
    factory<ContactUseCase> { ContactUseCaseImpl(get()) }
    factory<NoteUseCase> { NoteUseCaseImpl(get()) }
}