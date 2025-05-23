package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.domain.usecase.interfaces.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val userUseCase: UserUseCase
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _username = MutableStateFlow<String>("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun registerOnClick() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                if (username.value.isBlank() || password.value.isBlank()) {
                    _error.value = "Username and password cannot be empty"
                    _toastMessage.value = "Please fill in both username and password"
                    return@launch
                }

                val result = userUseCase.createUser(username.value, password.value)

                if (result.isSuccess) {
                    _toastMessage.value = "Account created successfully"
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                    _toastMessage.value = "An error occurred, please try again"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred during registration"
                _toastMessage.value = "An error occurred, please try again"
            } finally {
                _isLoading.value = false
            }
        }
    }
}