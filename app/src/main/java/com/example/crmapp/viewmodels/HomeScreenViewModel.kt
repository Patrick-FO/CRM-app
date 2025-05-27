package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.cache.ContactCacheManager
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.usecase.interfaces.UserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val userUseCase: UserUseCase,
    private val contactCacheManager: ContactCacheManager,
    private val appState: AppState
) : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val contactsList: StateFlow<List<ContactEntity>> = contactCacheManager.cachedContacts
    val isLoading: StateFlow<Boolean> = contactCacheManager.isLoading

    fun createContact(
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ) {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                contactCacheManager.createContact(
                    userId = userId,
                    name = name,
                    company = company,
                    phoneNumber = phoneNumber,
                    contactEmail = contactEmail
                ).fold(
                    onSuccess = {
                        _error.value = null
                    },
                    onFailure = {
                        _error.value = it.message ?: "Failed to create contact"
                    }
                )
            } else {
                _error.value = "User ID is null"
            }
        }
    }

    fun editContact(
        contactId: Int,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ) {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                contactCacheManager.editContact(
                    userId = userId,
                    contactId = contactId,
                    name = name,
                    company = company,
                    phoneNumber = phoneNumber,
                    contactEmail = contactEmail
                ).fold(
                    onSuccess = {
                        _error.value = null
                    },
                    onFailure = {
                        _error.value = it.message ?: "Failed to edit note"
                    }
                )
            } else {
                _error.value = "User ID is null"
            }
        }
    }

    fun deleteContact(contactId: Int) {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                delay(300)
                contactCacheManager.deleteContact(
                    userId = userId,
                    contactId = contactId
                ).fold(
                    onSuccess = {
                        _error.value = null
                    },
                    onFailure = {
                        _error.value = it.message ?: "Failed to delete note"
                    }
                )
            } else {
                _error.value = "User ID is null"
            }
        }
    }

    fun refreshContactsList() {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                contactCacheManager.getContacts(userId).fold(
                    onSuccess = {
                        _error.value = null
                    },
                    onFailure = {
                        _error.value = it.message ?: "Failed to load contacts"
                    }
                )
            } else {
                _error.value = "User ID is null"
            }
        }
    }



    fun logout() {
        viewModelScope.launch {
            try {
                userUseCase.logoutUser()
                contactCacheManager.clearCache()
                _error.value = null
            } catch(e: Exception) {
                _error.value = e.message ?: "Logout failed"
            }
        }
    }
}