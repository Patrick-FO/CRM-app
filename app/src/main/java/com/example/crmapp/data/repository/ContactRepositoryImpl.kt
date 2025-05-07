package com.example.crmapp.data.repository

import com.example.crmapp.api.interfaces.ContactApiService
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.repository.ContactRepository

class ContactRepositoryImpl(contactApiService: ContactApiService): ContactRepository {
    override suspend fun getAllContacts(userId: String): Result<List<ContactEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun createContact(
        userId: String,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<ContactEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun editContact(
        userId: String,
        contactId: Int,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteContact(userId: String, contactId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}