package com.example.crmapp.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.viewmodels.HomeScreenViewModel
import com.example.crmapp.views.composables.ContactCard
import com.example.crmapp.views.composables.ContactFormDialog
import com.example.crmapp.views.composables.CrmAppBar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    viewModel: HomeScreenViewModel,
    navController: NavController,
    appState: AppState
) {
    val selectedContact = remember { mutableStateOf<ContactEntity?>(null)}
    val showDialog = remember { mutableStateOf(false) }
    val showSpeedDial = remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (showSpeedDial.value) 45f else 0f,
        label = "fabRotation"
    )

    val fabColor by animateColorAsState(
        targetValue = if(showSpeedDial.value) Color(0xFFF44336) else Color(0xFF2196F3),
        label = "fabColor"
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
            FloatingActionButton(onClick = {
                selectedContact.value = null
                showDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add contact"
                )
            }
        }
    ) { paddingValues ->
        if(showDialog.value) {
            ContactFormDialog(onDismiss = { showDialog.value = false }, viewModel = viewModel, appState = appState, contactToEdit = selectedContact.value)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val contacts = viewModel.contactsList.collectAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(0.9f),
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
                            val alignment = Alignment.CenterEnd
                            Box(
                                Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                androidx.compose.material.Icon(Icons.Default.Delete, contentDescription = "Delete icon", tint = Color.White)
                            }
                        },
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = { FractionalThreshold(0.25f) },
                        dismissContent = {
                            ContactCard(
                                onEditClick = {
                                    selectedContact.value = contact
                                    showDialog.value = true
                                },
                                contact = contact,
                                onCardClick = {
                                    navController.navigate("contact_screen")
                                }
                            )
                        })
                }
            }
        }
    }
}