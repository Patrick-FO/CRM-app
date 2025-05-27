package com.example.crmapp.domain.cache

import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactCacheManager(
    private val contactUseCase: ContactUseCase
) {
    // Private mutable state for contacts - only this class can modify it
    private val _cachedContacts = MutableStateFlow<List<ContactEntity>>(emptyList())

    // Public read-only state that ViewModels can observe
    val cachedContacts: StateFlow<List<ContactEntity>> = _cachedContacts.asStateFlow()

    // Loading state shared across the app
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state shared across the app
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Track which user's data we have cached
    private var lastUserId: String? = null

    /**
     * Get contacts - uses cache if available for the same user, otherwise fetches fresh data
     */
    suspend fun getContacts(
        userId: String,
        forceRefresh: Boolean = false
    ): Result<List<ContactEntity>> {
        return if (!forceRefresh && _cachedContacts.value.isNotEmpty() && lastUserId == userId) {
            // We have cached data for this user - return it immediately
            Result.success(_cachedContacts.value)
        } else {
            // No cache or different user - fetch fresh data
            refreshContacts(userId)
        }
    }

    /**
     * Force refresh contacts from the API
     */
    suspend fun refreshContacts(userId: String): Result<List<ContactEntity>> {
        return try {
            _isLoading.value = true
            _error.value = null

            // Call your existing use case
            val result = contactUseCase.getAllContacts(userId)

            result.fold(
                onSuccess = { contacts ->
                    // Update cache with fresh data
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

    /**
     * Create a new contact and update the cache
     */
    suspend fun createContact(
        userId: String,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<Unit> {
        return try {
            _isLoading.value = true

            // Call your existing use case to create the contact
            contactUseCase.createContact(userId, name, company, phoneNumber, contactEmail)

            // Refresh cache to include the new contact
            refreshContacts(userId)

            Result.success(Unit)
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to create contact"
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * Edit an existing contact and update the cache
     */
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

            // Call your existing use case to edit the contact
            contactUseCase.editContact(userId, contactId, name, company, phoneNumber, contactEmail)

            // Refresh cache to reflect the changes
            refreshContacts(userId)

            Result.success(Unit)
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to edit contact"
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * Delete a contact and update the cache immediately for better UX
     */
    suspend fun deleteContact(userId: String, contactId: Int): Result<Unit> {
        return try {
            _isLoading.value = true

            // Call your existing use case to delete the contact
            contactUseCase.deleteContact(userId, contactId)

            // Immediately remove from cache for instant UI feedback
            _cachedContacts.value = _cachedContacts.value.filter { it.id != contactId }

            Result.success(Unit)
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to delete contact"
            // If delete failed, refresh cache to restore consistency
            refreshContacts(userId)
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * Get a specific contact by ID - tries cache first, falls back to API
     */
    suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity> {
        return try {
            // First, try to find the contact in our cache
            val cachedContact = _cachedContacts.value.find { it.id == contactId }

            if (cachedContact != null && lastUserId == userId) {
                // Found in cache - return immediately
                Result.success(cachedContact)
            } else {
                // Not in cache - call the API
                val result = contactUseCase.getContactById(userId, contactId)

                result.fold(
                    onSuccess = { contact ->
                        // Optionally add this contact to cache if we have cache for this user
                        if (lastUserId == userId) {
                            val currentContacts = _cachedContacts.value.toMutableList()
                            // Only add if it's not already in cache
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

    /**
     * Clear all cached data - call this on logout
     */
    fun clearCache() {
        _cachedContacts.value = emptyList()
        _error.value = null
        _isLoading.value = false
        lastUserId = null
    }

    /**
     * Check if we have cached data for a specific user
     */
    fun hasCachedDataForUser(userId: String): Boolean {
        return lastUserId == userId && _cachedContacts.value.isNotEmpty()
    }
}