package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.crmapp.domain.usecase.interfaces.UserUseCase

class SignUpViewModel(
    private val userUseCase: UserUseCase
): ViewModel() {

}