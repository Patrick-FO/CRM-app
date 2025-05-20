@file:Suppress("DEPRECATION")

package com.example.crmapp.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
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

    var name by remember { mutableStateOf(contactToEdit?.name ?: "")}
    var company by remember { mutableStateOf(contactToEdit?.company ?: "")}
    var phoneNumber by remember { mutableStateOf(contactToEdit?.phoneNumber ?: "")}
    var contactEmail by remember { mutableStateOf(contactToEdit?.contactEmail ?: "")}

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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

                Button(
                    onClick = {
                        if(appState.userId.value != null) {
                            if(isEditing) {
                                viewModel.editContact(
                                    contactId = contactToEdit!!.id,
                                    name = name,
                                    company = company.ifEmpty { null },
                                    phoneNumber = phoneNumber.ifEmpty { null },
                                    contactEmail = contactEmail.ifEmpty { null }
                                )
                            } else {
                                viewModel.createContact(
                                    name = name,
                                    company = company.ifEmpty { null },
                                    phoneNumber = phoneNumber.ifEmpty { null },
                                    contactEmail = contactEmail.ifEmpty { null }
                                )
                            }
                        }
                        onDismiss()
                        //TODO Add toast that says that an error with user ID not existing has occurred
                    }
                ) {
                    Text(text = if(isEditing) "Update" else "Create")
                }
            }
        }
    )
}