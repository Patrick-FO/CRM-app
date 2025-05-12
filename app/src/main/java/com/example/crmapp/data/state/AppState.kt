package com.example.crmapp.data.state

import com.example.crmapp.data.storage.JwtStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppState(
    private val jwtStorage: JwtStorage
) {
    private val _authState = MutableStateFlow<Boolean>(false)
    val authState: StateFlow<Boolean> = _authState.asStateFlow()

    fun setAuthState() {
        _authState.value = jwtStorage.hasJwt()
    }
}