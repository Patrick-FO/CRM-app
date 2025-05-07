package com.example.crmapp.data.repository

import com.example.crmapp.api.interfaces.ContactApiService
import com.example.crmapp.api.requests.ContactRequest
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.model.toDomain
import com.example.crmapp.domain.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactRepositoryImpl(private val contactApiService: ContactApiService): ContactRepository {
    override suspend fun getAllContacts(userId: String): Result<List<ContactEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val response = contactApiService.getAllContacts(userId)

                if(response.isSuccessful) {
                    val contacts = response.body()
                        ?: return@withContext Result.failure(Exception("Empty contacts response"))

                    val contactEntities = contacts.map { it.toDomain() }

                    Result.success(contactEntities)
                } else {
                    Result.failure(Exception("Failed to get contacts: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity> =
    withContext(Dispatchers.IO) {
        try {
            val response = contactApiService.getContactById(userId, contactId)

            if(response.isSuccessful) {
                val contact = response.body()
                    ?: return@withContext Result.failure(Exception("Empty contact response"))

                val contactEntity = contact.toDomain()

                Result.success(contactEntity)
            } else {
                Result.failure(Exception("Failed to get contact: ${response.code()} ${response.message()}"))
            }
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createContact(
        userId: String,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<ContactEntity> =
        withContext(Dispatchers.IO) {
        try {
            val request = ContactRequest(
                name,
                company,
                phoneNumber,
                contactEmail
            )
            val response = contactApiService.createContact(
                userId,
                request
            )

            if(response.isSuccessful) {
                val contact = response.body()
                    ?: return@withContext Result.failure(Exception("Empty contact: POST request failed"))

                val contactEntity = contact.toDomain()

                Result.success(contactEntity)
            } else {
                Result.failure(Exception("Failed to create contact: ${response.code()} ${response.message()}"))
            }
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editContact(
        userId: String,
        contactId: Int,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val request = ContactRequest(
                    name,
                    company,
                    phoneNumber,
                    contactEmail
                )
                val response = contactApiService.editContact(userId, contactId, request)

                if(response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to edit contact: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }

    override suspend fun deleteContact(userId: String, contactId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val response = contactApiService.deleteContact(userId, contactId)

                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to delete contact: ${response.code()} ${response.message()}"))
                }
            } catch(e: Exception) {
                Result.failure(e)
            }
    }
}