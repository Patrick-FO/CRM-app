package com.example.crmapp.di.modules

import com.example.crmapp.data.state.AppState
import org.koin.dsl.module

val stateModule = module {
    single { AppState(get()) }
}