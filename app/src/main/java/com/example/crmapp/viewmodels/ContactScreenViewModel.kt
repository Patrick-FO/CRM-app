package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.model.entities.NoteEntity
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import com.example.crmapp.domain.usecase.interfaces.NoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactScreenViewModel(
    private val contactUseCase: ContactUseCase,
    private val noteUseCase: NoteUseCase,
    private val appState: AppState
): ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _contactName = MutableStateFlow<String?>(null)
    val contactName = _contactName.asStateFlow()

    private var _notesList = MutableStateFlow<List<NoteEntity>>(listOf())
    val notesList = _notesList.asStateFlow()

    private var _contactsList = MutableStateFlow<List<ContactEntity>>(listOf())
    val contactsList = _contactsList.asStateFlow()

    private var currentContactId: Int? = null

    fun loadContactData(contactId: Int) {
        currentContactId = contactId
        getContactName(contactId)
        refreshNotesList(contactId)
    }

    fun clearData() {
        _contactName.value = null
        _notesList.value = emptyList()
        _error.value = null
        currentContactId = null
    }

    private fun getContactName(contactId: Int) {
        val userId = appState.userId.value
        viewModelScope.launch {
            _isLoading.value = true
            if(userId != null) {
                try {
                    contactUseCase.getContactById(userId, contactId).fold(
                        onSuccess = {
                            _contactName.value = it.name
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "Failed to load contact name"
                        }
                    )
                } catch(e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }
            } else {
                _error.value = "User ID is empty"
            }
            _isLoading.value = false
        }
    }

    private fun refreshNotesList(contactId: Int) {
        val userId = appState.userId.value

        viewModelScope.launch {
            _isLoading.value = true
            if(userId != null) {
                try {
                    noteUseCase.getNotesForContact(userId, contactId).fold(
                        onSuccess = { result ->
                            _notesList.value = result
                        },
                        onFailure = { exception ->
                            _error.value = exception.message
                        }
                    )
                } catch(e: Exception) {
                    _error.value = e.message
                }
            } else {
                _error.value = "User ID is empty"
            }
            _isLoading.value = false
        }
    }

    fun createNote(
        contactIds: List<Int>,
        title: String,
        description: String?
    ) {
        val userId = appState.userId.value

        viewModelScope.launch {
            _isLoading.value = true

            if(userId != null) {
                try {
                    noteUseCase.createNote(
                        userId = userId,
                        contactIds = contactIds,
                        title = title,
                        description = description ?: ""
                    )
                    // Refresh the notes list if we're viewing a contact that's included in the note
                    currentContactId?.let { contactId ->
                        if (contactIds.contains(contactId)) {
                            refreshNotesList(contactId)
                        }
                    }
                } catch(e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }
            } else {
                _error.value = "User ID is empty"
            }

            _isLoading.value = false
        }
    }

    fun editNote(
        noteId: Int,
        contactIds: List<Int>,
        title: String,
        description: String?
    ) {
        val userId = appState.userId.value

        viewModelScope.launch {
            _isLoading.value = true

            try {
                if(userId != null && noteId >= 1) {
                    try {
                        noteUseCase.editNote(userId, noteId, contactIds, title, description)
                        // Refresh the notes list for the current contact
                        currentContactId?.let { contactId ->
                            refreshNotesList(contactId)
                        }
                    } catch(e: Exception) {
                        _error.value = e.message
                    }
                } else {
                    _error.value = "User ID or note ID is invalid"
                }
            } catch(e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            }

            _isLoading.value = false
        }
    }

    fun deleteNote(noteId: Int) {
        val userId = appState.userId.value

        viewModelScope.launch {
            _isLoading.value = true

            if (userId != null) {
                try {
                    noteUseCase.deleteNote(userId, noteId)
                    // Refresh the notes list for the current contact
                    currentContactId?.let { contactId ->
                        refreshNotesList(contactId)
                    }
                } catch (e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }
            } else {
                _error.value = "User ID is empty"
            }

            _isLoading.value = false
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
}