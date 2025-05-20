package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import com.example.crmapp.domain.usecase.interfaces.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val userUseCase: UserUseCase,
    private val contactUseCase: ContactUseCase,
    private val appState: AppState
) : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _contactsList = MutableStateFlow<List<ContactEntity>>(listOf())
    val contactsList = _contactsList.asStateFlow()

    init {
        refreshContactsList()
    }

    fun createContact(
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ) {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                _isLoading.value = true

                try {
                    contactUseCase.createContact(
                        userId = userId,
                        name = name,
                        company = company,
                        phoneNumber = phoneNumber,
                        contactEmail = contactEmail
                    )

                    refreshContactsList()
                } catch(e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }

                _isLoading.value = false
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
                _isLoading.value = true

                try {
                    contactUseCase.editContact(
                        userId = userId,
                        contactId = contactId,
                        name = name,
                        company = company,
                        phoneNumber = phoneNumber,
                        contactEmail = contactEmail
                    )

                    refreshContactsList()
                } catch(e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }

                _isLoading.value = false
            }
        }
    }

    fun deleteContact(contactId: Int) {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                _isLoading.value = true

                try {
                    contactUseCase.deleteContact(
                        userId = userId,
                        contactId = contactId
                    )

                    refreshContactsList()
                } catch(e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }
                _isLoading.value = false
            }
        }
    }

    private fun refreshContactsList() {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                _isLoading.value = true
                try {
                    val result = contactUseCase.getAllContacts(userId)

                    result.fold(
                        onSuccess = { contacts ->
                            _contactsList.value = contacts
                            _error.value = null
                        },
                        onFailure = {
                            exception ->
                            _error.value = exception.message ?: "Failed to load contacts"
                        }
                    )
                } catch(e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                } finally {
                    _isLoading.value = false
                }
            } else {
                _error.value = "Contacts list can't be refreshed when user ID is null"
            }
        }
    }

    fun logout() {
        val userId = appState.userId.value

        viewModelScope.launch {
            if(userId != null) {
                _isLoading.value = true

                //TODO Improve error handling
                try {
                    userUseCase.logoutUser()
                } catch(e: Exception) {
                    _error.value = e.message
                }
            } else {
                _error.value = "Can't logout when there's no user ID"
            }
        }
    }
}