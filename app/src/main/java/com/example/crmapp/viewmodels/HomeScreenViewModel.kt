package com.example.crmapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import com.example.crmapp.domain.usecase.interfaces.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val contactUseCase: ContactUseCase,
    private val appState: AppState
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _contactsState = MutableStateFlow<ContactsState>(ContactsState.Loading)
    val contactsState: StateFlow<ContactsState> = _contactsState.asStateFlow()

    var isModalVisible by mutableStateOf(false)
        private set

    var newContactName by mutableStateOf("")
        private set
    var newContactCompany by mutableStateOf("")
        private set
    var newContactPhone by mutableStateOf("")
        private set
    var newContactEmail by mutableStateOf("")
        private set

    // Get the current user ID from the session manager
    private val userId = appState.userId.value

    // Function to load contacts for the current user
    fun loadContacts() {
        viewModelScope.launch {
            _contactsState.value = ContactsState.Loading

            if(userId == null) {
                _error.value = "User ID is null"
            } else {
                contactUseCase.getAllContacts(userId)
                    .onSuccess { contacts ->
                        _contactsState.value = ContactsState.Success(contacts)
                    }
                    .onFailure { error ->
                        _contactsState.value = ContactsState.Error(error.message ?: "Unknown error")
                    }
            }
        }
    }

    // Function to create a new contact
    fun createContact() {
        if (newContactName.isBlank()) {
            _contactsState.value = ContactsState.Error("Name cannot be empty")
            return
        }

        viewModelScope.launch {
            val currentState = _contactsState.value
            _contactsState.value = ContactsState.Loading

            if(userId == null) {
                _error.value = "User ID is null"
            } else {
                contactUseCase.createContact(
                    userId = userId,
                    name = newContactName,
                    company = if (newContactCompany.isNotBlank()) newContactCompany else null,
                    phoneNumber = if (newContactPhone.isNotBlank()) newContactPhone else null,
                    contactEmail = if (newContactEmail.isNotBlank()) newContactEmail else null
                ).onSuccess {
                    clearNewContactForm()
                    loadContacts()  // Reload the contacts list
                    toggleModal() // Close the modal after creating
                }.onFailure { error ->
                    _contactsState.value = currentState
                    // You might want to add a specific error state for form errors
                }
            }
        }
    }

    // Function to delete a contact
    fun deleteContact(contactId: Int) {
        viewModelScope.launch {
            val currentState = _contactsState.value
            if(userId == null) {
                _error.value = "User ID is null"
            } else {
            contactUseCase.deleteContact(userId, contactId)
                .onSuccess {
                    loadContacts()
                }
                .onFailure { error ->
                    _contactsState.value =
                        ContactsState.Error(error.message ?: "Failed to delete contact")
                }
            }
        }
    }

    // Functions to toggle the modal visibility
    fun toggleModal() {
        isModalVisible = !isModalVisible
    }

    // Function to update new contact form fields
    fun updateNewContactName(name: String) {
        newContactName = name
    }

    fun updateNewContactCompany(company: String) {
        newContactCompany = company
    }

    fun updateNewContactPhone(phone: String) {
        newContactPhone = phone
    }

    fun updateNewContactEmail(email: String) {
        newContactEmail = email
    }

    // Function to clear the new contact form
    private fun clearNewContactForm() {
        newContactName = ""
        newContactCompany = ""
        newContactPhone = ""
        newContactEmail = ""
    }

    // Sealed class to represent the different states of the contacts list
    sealed class ContactsState {
        object Loading : ContactsState()
        data class Success(val contacts: List<ContactEntity>) : ContactsState()
        data class Error(val message: String) : ContactsState()
    }
}