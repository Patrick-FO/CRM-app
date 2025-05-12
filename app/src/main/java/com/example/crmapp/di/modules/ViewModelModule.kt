package com.example.crmapp.di.modules

import com.example.crmapp.viewmodels.ContactScreenViewModel
import com.example.crmapp.viewmodels.HomeScreenViewModel
import com.example.crmapp.viewmodels.LoginViewModel
import com.example.crmapp.viewmodels.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ContactScreenViewModel(get(), get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get()) }
}