@file:Suppress("DEPRECATION")

package com.example.crmapp.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crmapp.data.state.AppState
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.navigation.Screen
import com.example.crmapp.viewmodels.ContactScreenViewModel
import com.example.crmapp.viewmodels.HomeScreenViewModel
import com.example.crmapp.views.composables.ContactCard
import com.example.crmapp.views.composables.ContactFormDialog
import com.example.crmapp.views.composables.CrmAppBar
import com.example.crmapp.views.composables.NoteFormDialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    viewModel: HomeScreenViewModel,
    navController: NavController,
    appState: AppState
) {
    val selectedContact = remember { mutableStateOf<ContactEntity?>(null)}
    val showContactDialog = remember { mutableStateOf(false) }
    val showNoteCreationDialog = remember { mutableStateOf(false) }
    val showSpeedDial = remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (showSpeedDial.value) 45f else 0f,
        label = "fabRotation"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CrmAppBar(title = "Contacts", onBackButtonClick = {
                viewModel.logout()
                navController.navigate("login_screen")
            })
        },
        floatingActionButton = {
            Box(contentAlignment = Alignment.BottomEnd) {
                AnimatedVisibility(
                    visible = showSpeedDial.value,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it }
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 80.dp, end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                shape = RoundedCornerShape(4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = "Add Contact",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            SmallFloatingActionButton(
                                onClick = {
                                    selectedContact.value = null
                                    showContactDialog.value = true
                                    showSpeedDial.value = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Add Contact"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                shape = RoundedCornerShape(4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = "Add Note",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            SmallFloatingActionButton(
                                onClick = {
                                    showNoteCreationDialog.value = true
                                    showSpeedDial.value = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Add Note"
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                        showSpeedDial.value = !showSpeedDial.value
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.rotate(rotationAngle)
                    )
                }
            }
        }
    ) { paddingValues ->
        if(showContactDialog.value) {
            ContactFormDialog(onDismiss = { showContactDialog.value = false }, viewModel = viewModel, appState = appState, contactToEdit = selectedContact.value)
        }

        if(showNoteCreationDialog.value) {
            NoteFormDialog(
                onDismiss = { showNoteCreationDialog.value = false },
                appState = appState
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val contacts = viewModel.contactsList.collectAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = contacts.value,
                    key = {contact -> contact.id}
                ) { contact ->

                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if(it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                viewModel.deleteContact(contact.id)
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
                            ContactCard(
                                onEditClick = {
                                    selectedContact.value = contact
                                    showContactDialog.value = true
                                },
                                contact = contact,
                                onCardClick = {
                                    navController.navigate(Screen.ContactScreen.createRoute(contact.id))
                                }
                            )
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}