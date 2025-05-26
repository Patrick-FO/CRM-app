package com.example.crmapp.views.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.ContactEntity

@Composable
fun ContactCard(
    onEditClick: () -> Unit,
    onCardClick: () -> Unit,
    contact: ContactEntity,
) {
    Card(modifier = Modifier
        .padding(horizontal = 16.dp)
        .clickable {
            onCardClick()
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = contact.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
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

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit contact",
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        onEditClick()
                    }
            )
        }
    }
}