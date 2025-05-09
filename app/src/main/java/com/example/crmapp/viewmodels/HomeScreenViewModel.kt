package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase

class HomeScreenViewModel(
    private val contactUseCase: ContactUseCase
): ViewModel() {
}