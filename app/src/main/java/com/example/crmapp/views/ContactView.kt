package com.example.crmapp.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.NoteEntity
import com.example.crmapp.viewmodels.ContactScreenViewModel
import com.example.crmapp.viewmodels.HomeScreenViewModel
import com.example.crmapp.views.composables.CrmAppBar
import com.example.crmapp.views.composables.NoteCard
import com.example.crmapp.views.composables.NoteFormDialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ContactView(
    viewModel: ContactScreenViewModel,
    navController: NavController,
    appState: AppState,
    contactId: Int
) {
    val contactName by viewModel.contactName.collectAsState()
    val notesList by viewModel.notesList.collectAsState()
    //TODO Get selected contact id from home view passed in navigation

    val showNoteEditDialog = remember { mutableStateOf(false) }
    val selectedNoteForEdit = remember { mutableStateOf<NoteEntity?>(null) }

    LaunchedEffect(selectedContactId) {
        if(selectedContactId.value != null) {
            viewModel.loadContactData()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearData()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CrmAppBar(
                title = contactName ?: "Loading contact name...",
                onBackButtonClick = {
                    navController.navigate("home_screen")
                }
            )
        }
    ) { paddingValues ->

        if (showNoteEditDialog.value && selectedNoteForEdit.value != null) {
            NoteFormDialog(
                onDismiss = {
                    showNoteEditDialog.value = false
                    selectedNoteForEdit.value = null
                },
                appState = appState,
                noteToEdit = selectedNoteForEdit.value,
                preSelectedContactId = selectedContactId.value
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = if (notesList.isEmpty()) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (notesList.isEmpty()) {
                Text(
                    text = "No notes for this contact yet",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        items = notesList,
                        key = { note -> note.id }
                    ) { note ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                if (dismissValue == DismissValue.DismissedToEnd || dismissValue == DismissValue.DismissedToStart) {
                                    viewModel.deleteNote(note.id)
                                }
                                true
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                val color by animateColorAsState(
                                    if(dismissState.dismissDirection == DismissDirection.EndToStart) Color.Red else Color.Transparent,
                                    label = ""
                                )

                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 30.dp)
                                            .background(color)
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        androidx.compose.material.Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete icon",
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { FractionalThreshold(0.25f) },
                            dismissContent = {
                                NoteCard(
                                    onEditClick = {
                                        showNoteEditDialog.value = true
                                        selectedNoteForEdit.value = note
                                    },
                                    note = note,
                                )
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}