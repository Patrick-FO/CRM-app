package com.example.crmapp.domain.model

import com.example.crmapp.api.responses.ContactResponse
import com.example.crmapp.api.responses.NoteResponse
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.domain.model.entities.NoteEntity

fun ContactResponse.toDomain(): ContactEntity {
    return ContactEntity(
        id = this.id,
        name = this.name,
        userId = this.userId,
        company = this.company,
        phoneNumber = this.phoneNumber,
        contactEmail = this.contactEmail
    )
}

fun NoteResponse.toDomain(): NoteEntity {
    return NoteEntity(
        id = this.id,
        userId = this.userId,
        contactIds = this.contactIds,
        title = this.title,
        description = this.description
    )
}