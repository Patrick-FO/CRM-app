package com.example.crmapp.views.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrmAppBar(title: String, onBackButtonClick: () -> Unit) {

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if(title.contains("Contacts")) {
                IconButton(onClick = { onBackButtonClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        tint = Color.Black,
                        contentDescription = null
                    )
                }
            } else {
                IconButton(onClick = { onBackButtonClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        }
    )
}