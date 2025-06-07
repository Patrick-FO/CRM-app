@file:Suppress("DEPRECATION")

package com.example.crmapp.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormDialog(
    onDismiss: () -> Unit,
    viewModel: HomeScreenViewModel,
    appState: AppState,
    contactToEdit: ContactEntity? = null
) {
    val isEditing = contactToEdit != null

    var name by remember { mutableStateOf(contactToEdit?.name ?: "") }
    var company by remember { mutableStateOf(contactToEdit?.company ?: "") }
    var phoneNumber by remember { mutableStateOf(contactToEdit?.phoneNumber ?: "")}
    var contactEmail by remember { mutableStateOf(contactToEdit?.contactEmail ?: "")}

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if(isEditing) {
                            "Edit contact"
                        } else {
                            "Create contact"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss dialog",
                        modifier = Modifier
                            .clickable {
                                onDismiss()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Name") }
                )

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text(text = "Company") }
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text(text = "Phone number") }
                )

                OutlinedTextField(
                    value = contactEmail,
                    onValueChange = { contactEmail = it },
                    label = { Text(text = "Contact email") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if(appState.userId.value != null) {
                            if(isEditing) {
                                viewModel.editContact(
                                    contactId = contactToEdit!!.id,
                                    name = name,
                                    company = if (company.isBlank()) null else company,
                                    phoneNumber = if (phoneNumber.isBlank()) null else phoneNumber,
                                    contactEmail = if (contactEmail.isBlank()) null else contactEmail
                                )
                            } else {
                                viewModel.createContact(
                                    name = name,
                                    company = if (company.isBlank()) null else company,
                                    phoneNumber = if (phoneNumber.isBlank()) null else phoneNumber,
                                    contactEmail = if (contactEmail.isBlank()) null else contactEmail
                                )
                            }
                        }
                        onDismiss()
                        //TODO Add toast that says that an error with user ID not existing has occurred
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFFE0E0E0),
                        disabledContentColor = Color(0xFF9E9E9E)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if(isEditing) "Update Contact" else "Create Contact",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                imageVector = if (isEditing) {
                                    Icons.Default.Edit
                                } else {
                                    Icons.Default.Add
                                },
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}