@file:Suppress("DEPRECATION")
package com.example.crmapp.views.composables

import com.example.crmapp.domain.model.entities.NoteEntity
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
import com.example.crmapp.viewmodels.ContactScreenViewModel
import com.example.crmapp.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormDialog(
    onDismiss: () -> Unit,
    contactViewModel: HomeScreenViewModel,
    noteViewModel: ContactScreenViewModel,
    appState: AppState,
    noteToEdit: NoteEntity? = null
) {
    val isEditing = noteToEdit != null

    var title by remember { mutableStateOf(noteToEdit?.title ?: "")}
    var contactIds by remember { mutableStateOf(noteToEdit?.contactIds ?: "")}
    var description by remember { mutableStateOf(noteToEdit?.description ?: "")}

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
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title") }
                )

                //TODO Lazy row with all of our contacts

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Description") }
                )

                Button(
                    onClick = {
                        if(appState.userId.value != null) {
                            if(isEditing) {
                                viewModel.editContact(
                                    contactId = noteToEdit!!.id,
                                    name = name,
                                    description = company.ifEmpty { null },
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