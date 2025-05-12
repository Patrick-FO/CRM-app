package com.example.crmapp.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.crmapp.viewmodels.SignupViewModel

@Composable
fun SignupView(
    viewModel: SignupViewModel,
    navController: NavController
) {
    val toastMessage by viewModel.toastMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Create a new account")

        OutlinedTextField(
            value = viewModel.username.collectAsState().value,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text(text = "Username") },
            enabled = !isLoading
        )

        OutlinedTextField(
            value = viewModel.password.collectAsState().value,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        Button(
            onClick = {
                viewModel.registerOnClick()
                navController.navigate("login_screen")
                      },
            enabled = !isLoading
        ) {
            Text(text = "Register")
        }

        Button(
            onClick = { navController.navigate("login_screen") },
            enabled = !isLoading
        ) {
            Text(text = "Already have an account?")
        }
    }
}