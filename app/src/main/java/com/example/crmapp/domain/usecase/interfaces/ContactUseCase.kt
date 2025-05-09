package com.example.crmapp.domain.usecase.interfaces

import com.example.crmapp.domain.model.entities.ContactEntity

interface ContactUseCase {
    suspend fun getAllContacts(userId: String): Result<List<ContactEntity>>

    //TODO Might not be needed, as we will have another use case that both gets the contact and notes
    suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity>

    suspend fun createContact(userId: String, name: String, company: String?, phoneNumber: String?, contactEmail: String?): Result<ContactEntity>

    suspend fun editContact(userId: String, contactId: Int, name: String, company: String?, phoneNumber: String?, contactEmail: String?): Result<Boolean>

    suspend fun deleteContact(userId: String, contactId: Int): Result<Boolean>
}