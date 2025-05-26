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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.NoteEntity
import com.example.crmapp.viewmodels.ContactScreenViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormDialog(
    onDismiss: () -> Unit,
    appState: AppState,
    noteToEdit: NoteEntity? = null,
    preSelectedContactId: Int? = null
) {
    val viewModel: ContactScreenViewModel = getViewModel()

    val isEditing = noteToEdit != null
    val contacts = viewModel.contactsList
    //TODO Get list of contacts from viewmodel

    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var description by remember { mutableStateOf(noteToEdit?.description ?: "") }

    var selectedContactIds by remember {
        mutableStateOf(
            when {
                noteToEdit != null -> noteToEdit.contactIds.toSet()
                preSelectedContactId != null -> setOf(preSelectedContactId)
                else -> emptySet()
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
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

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(
                        text = "Select Contacts:",
                        style = MaterialTheme.typography.labelLarge
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = contacts,
                            key = { contact -> contact.id }
                        ) { contact ->
                            FilterChip(
                                onClick = {
                                    selectedContactIds = if (selectedContactIds.contains(contact.id)) {
                                        selectedContactIds - contact.id
                                    } else {
                                        selectedContactIds + contact.id
                                    }
                                },
                                label = { Text(contact.name) },
                                selected = selectedContactIds.contains(contact.id)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Button(
                    onClick = {
                        if (appState.userId.value != null && selectedContactIds.isNotEmpty() && title.isNotBlank()) {
                            if (isEditing && noteToEdit != null) {
                                viewModel.editNote(
                                    noteId = noteToEdit.id,
                                    contactIds = selectedContactIds.toList(),
                                    title = title,
                                    description = description.ifEmpty { null }
                                )
                            } else {
                                viewModel.createNote(
                                    contactIds = selectedContactIds.toList(),
                                    title = title,
                                    description = description.ifEmpty { null }
                                )
                            }
                            onDismiss()
                        }
                        // TODO: Add proper validation and error handling
                    },
                    enabled = selectedContactIds.isNotEmpty() && title.isNotBlank(),
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
                            text = if(isEditing) "Update Note" else "Create Note",
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