package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crmapp.data.state.AppState
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

    fun loadContactData() {
        getContactName()
        refreshNotesList()
    }

    fun clearData() {
        _contactName.value = null
        _notesList.value = emptyList()
        _error.value = null
    }

    //TODO Go look over this function and make sure its good
    fun getContactName() {
        val userId = appState.userId.value
        val contactId = appState.selectedContactId.value
        viewModelScope.launch {
            _isLoading.value = true
            if(userId != null && contactId != null) {
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
                _error.value = "User ID or contact ID is empty"
            }
            _isLoading.value = false
        }
    }

    fun refreshNotesList() {
        val userId = appState.userId.value
        val contactId = appState.selectedContactId.value

        viewModelScope.launch {
            _isLoading.value = true
            if(userId != null && contactId != null) {
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
                _error.value = "User ID or contact ID is empty"
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
                    //TODO Maybe give function below a purpose by implementing ability to add notes directly on the contact screen?
                    //refreshNotesList()
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
                        refreshNotesList()
                    } catch(e: Exception) {
                        _error.value = e.message
                    }
                } else {
                    _error.value = "User ID or contact ID is empty"
                }
            } catch(e: Exception) {
                _error.value = e.message ?: "An unexpected error occured"
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
                    refreshNotesList()
                } catch (e: Exception) {
                    _error.value = e.message ?: "An unexpected error occurred"
                }
            } else {
                _error.value = "User ID is empty"
            }

            _isLoading.value = false
        }
    }
}