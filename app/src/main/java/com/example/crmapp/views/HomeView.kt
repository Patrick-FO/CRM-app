package com.example.crmapp.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crmapp.domain.model.entities.ContactEntity
import com.example.crmapp.viewmodels.HomeScreenViewModel
import com.example.crmapp.views.composables.ContactFormModal
import com.example.crmapp.views.composables.CrmTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
    // Collect the contacts state from the ViewModel
    val contactsState by viewModel.contactsState.collectAsState()

    // Load contacts when the screen is first displayed
    LaunchedEffect(key1 = true) {
        viewModel.loadContacts()
    }

    Scaffold(
        topBar = {
            CrmTopAppBar(
                onMenuClick = { viewModel.toggleModal() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.toggleModal() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Contact"
                )
            }
        }
    ) { paddingValues ->
        // Main content with the contacts list
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = contactsState) {
                is HomeScreenViewModel.ContactsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HomeScreenViewModel.ContactsState.Success -> {
                    val contacts = state.contacts

                    if (contacts.isEmpty()) {
                        // Show message when there are no contacts
                        Text(
                            text = "No contacts found. Add your first contact!",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    } else {
                        // Show the contacts list
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(contacts) { contact ->
                                ContactCard(
                                    contact = contact,
                                    onDeleteClick = {
                                        viewModel.deleteContact(contact.id)
                                    },
                                    onContactClick = {
                                        // Navigate to contact detail screen
                                        navController.navigate("contact_screen/${contact.id}")
                                    },
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                is HomeScreenViewModel.ContactsState.Error -> {
                    // Show error message
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }

            // Modal sheet for adding new contacts
            if (viewModel.isModalVisible) {
                ContactFormModal(
                    isVisible = viewModel.isModalVisible,
                    onDismiss = { viewModel.toggleModal() },
                    name = viewModel.newContactName,
                    company = viewModel.newContactCompany,
                    phone = viewModel.newContactPhone,
                    email = viewModel.newContactEmail,
                    onNameChange = viewModel::updateNewContactName,
                    onCompanyChange = viewModel::updateNewContactCompany,
                    onPhoneChange = viewModel::updateNewContactPhone,
                    onEmailChange = viewModel::updateNewContactEmail,
                    onSave = { viewModel.createContact() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun ContactCard(
    contact: ContactEntity,
    onDeleteClick: () -> Unit,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxSize(),
        onClick = onContactClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )

            contact.company?.let {
                if (it.isNotBlank()) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            contact.phoneNumber?.let {
                if (it.isNotBlank()) {
                    Text(
                        text = "Phone: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            contact.contactEmail?.let {
                if (it.isNotBlank()) {
                    Text(
                        text = "Email: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Delete button
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Contact"
                    )
                }
            }
        }
    }
}