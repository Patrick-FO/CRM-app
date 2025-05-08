package com.example.crmapp.domain.usecase.impl

import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.repository.ContactRepository
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase

class ContactUseCaseImpl(
    private val contactRepository: ContactRepository
): ContactUseCase {
    override suspend fun getAllContacts(userId: String): Result<List<ContactEntity>> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        return contactRepository.getAllContacts(userId)
    }

    override suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(contactId <= 0)(
            return Result.failure(IllegalArgumentException("Contact ID must be 1 or greater"))
        )

        return contactRepository.getContactById(userId, contactId)
    }

    override suspend fun createContact(
        userId: String,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<ContactEntity> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name must not be blank"))
        }

        return contactRepository.createContact(userId, name, company, phoneNumber, contactEmail)
    }

    override suspend fun editContact(
        userId: String,
        contactId: Int,
        name: String,
        company: String?,
        phoneNumber: String?,
        contactEmail: String?
    ): Result<Boolean> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(contactId <= 0) {
            return Result.failure(IllegalArgumentException("Contact ID must be 1 or greater"))
        }

        if(name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name must not be blank"))
        }

        return contactRepository.editContact(userId, contactId, name, company, phoneNumber, contactEmail)
    }

    override suspend fun deleteContact(userId: String, contactId: Int): Result<Boolean> {
        if(userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID must not be blank"))
        }

        if(contactId <= 0) {
            return Result.failure(IllegalArgumentException("Contact ID must be 1 or greater"))
        }

        return contactRepository.deleteContact(userId, contactId)
    }
}