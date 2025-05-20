package com.example.crmapp.views.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crmapp.domain.model.entities.ContactEntity

@Composable
fun ContactCard(
    onEditClick: () -> Unit,
    onCardClick: () -> Unit,
    contact: ContactEntity
) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { onCardClick() }) {
        Row() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = contact.name)
                if (contact.company != null) {
                    Text(text = contact.company)
                }
                if (contact.phoneNumber != null) {
                    Text(text = contact.phoneNumber)
                }
                if (contact.contactEmail != null) {
                    Text(text = contact.contactEmail)
                }
            }
            Button(onClick = { onEditClick() }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit contact"
                )
            }
        }
    }
}