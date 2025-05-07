package com.example.crmapp.domain.model

import com.example.crmapp.api.responses.ContactResponse
import com.example.crmapp.domain.model.entities.ContactEntity

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