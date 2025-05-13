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
import com.example.crmapp.data.state.AppState
import com.example.crmapp.viewmodels.LoginViewModel

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    navController: NavController,
    appState: AppState
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
        Text(text = "Login")

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
                viewModel.loginOnClick()
                if(appState.authState.value) {
                    navController.navigate("home_screen")
                }
                      },
            enabled = !isLoading
        ) {
            Text(text = "Login")
        }

        Button(
            onClick = { navController.navigate("signup_screen") },
            enabled = !isLoading
        ) {
            Text(text = "Don't have an account?")
        }
    }
}