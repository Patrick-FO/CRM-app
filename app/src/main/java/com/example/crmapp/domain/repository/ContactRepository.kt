package com.example.crmapp.domain.repository

import com.example.crmapp.domain.model.entities.ContactEntity
import java.util.UUID

interface ContactRepository {
    suspend fun getAllContacts(userId: String): Result<List<ContactEntity>>

    suspend fun getContactById(userId: String, contactId: Int): Result<ContactEntity>

    suspend fun createContact(userId: String, name: String, company: String?, phoneNumber: String?, contactEmail: String?): Result<ContactEntity>

    suspend fun editContact(userId: String, contactId: Int, name: String, company: String?, phoneNumber: String?, contactEmail: String?): Result<Boolean>

    suspend fun deleteContact(userId: String, contactId: Int): Result<Boolean>
}