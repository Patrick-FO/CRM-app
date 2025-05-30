package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.usecase.interfaces.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userUseCase: UserUseCase,
    private val appState: AppState
) : ViewModel() {

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

    fun loginOnClick(onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                if (username.value.isBlank() || password.value.isBlank()) {
                    _error.value = "Username and password cannot be empty"
                    _toastMessage.value = "Please fill in both username and password"
                    onComplete(false)
                    return@launch
                }

                val result = userUseCase.loginUser(username.value, password.value)

                if (result.isSuccess) {
                    appState.setUserId(
                        userUseCase.getUserId(username.value, password.value).getOrNull()
                    )

                    _toastMessage.value = "Successfully logged in"
                    _password.value = ""
                    onComplete(true)
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                    _toastMessage.value = "An error occurred, please try again"
                    onComplete(false)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred during login"
                _toastMessage.value = "An error occurred, please try again"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}