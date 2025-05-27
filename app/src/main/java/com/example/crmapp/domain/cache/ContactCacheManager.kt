package com.example.crmapp.domain.cache

import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactCacheManager(
    private val contactUseCase: ContactUseCase
) {
    private val _cachedContacts = MutableStateFlow<List<ContactEntity>>(emptyList())

    val cachedContacts: StateFlow<List<ContactEntity>> = _cachedContacts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var lastUserId: String? = null

    suspend fun getContacts(
        userId: String,
        forceRefresh: Boolean = false
    ): Result<List<ContactEntity>> {
        return if (!forceRefresh && _cachedContacts.value.isNotEmpty() && lastUserId == userId) {
            Result.success(_cachedContacts.value)
        } else {
            refreshContacts(userId)
        }
    }

    suspend fun refreshContacts(userId: String): Result<List<ContactEntity>> {
        return try {
            _isLoading.value = true
            _error.value = null

            val result = contactUseCase.getAllContacts(userId)

            result.fold(
                onSuccess = { contacts ->
                    _cachedContacts.value = contacts
                    lastUserId = userId
                    _error.value = null
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Failed to load contacts"
                }
            )

            result
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unexpected error occurred"
            _error.value = errorMessage
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun createContact(
        userId: String,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<Unit> {
        return try {
            _isLoading.value = true

            contactUseCase.createContact(userId, name, company, phoneNumber, contactEmail)

            refreshContacts(userId)

            Result.success(Unit)
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to create contact"
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun editContact(
        userId: String,
        contactId: Int,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<Unit> {
        return try {
            _isLoading.value = true

            contactUseCase.editContact(userId, contactId, name, company, phoneNumber, contactEmail)

            refreshContacts(userId)

            Result.success(Unit)
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to edit contact"
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun deleteContact(userId: String, contactId: Int): Result<Unit> {
        return try {
            _isLoading.value = true

            contactUseCase.deleteContact(userId, contactId)

            _cachedContacts.value = _cachedContacts.value.filter { it.id != contactId }

            Result.success(Unit)
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to delete contact"
            refreshContacts(userId)
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity> {
        return try {
            val cachedContact = _cachedContacts.value.find { it.id == contactId }

            if (cachedContact != null && lastUserId == userId) {
                Result.success(cachedContact)
            } else {
                val result = contactUseCase.getContactById(userId, contactId)

                result.fold(
                    onSuccess = { contact ->
                        if (lastUserId == userId) {
                            val currentContacts = _cachedContacts.value.toMutableList()
                            if (currentContacts.none { it.id == contact.id }) {
                                currentContacts.add(contact)
                                _cachedContacts.value = currentContacts
                            }
                        }
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Failed to get contact"
                    }
                )

                result
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to get contact"
            Result.failure(e)
        }
    }

    fun clearCache() {
        _cachedContacts.value = emptyList()
        _error.value = null
        _isLoading.value = false
        lastUserId = null
    }

    fun hasCachedDataForUser(userId: String): Boolean {
        return lastUserId == userId && _cachedContacts.value.isNotEmpty()
    }
}