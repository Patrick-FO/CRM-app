package com.example.crmapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crmapp.data.state.AppState
import com.example.crmapp.viewmodels.ContactScreenViewModel
import com.example.crmapp.viewmodels.HomeScreenViewModel
import com.example.crmapp.viewmodels.LoginViewModel
import com.example.crmapp.viewmodels.SignupViewModel
import com.example.crmapp.views.ContactView
import com.example.crmapp.views.HomeView
import com.example.crmapp.views.LoginView
import com.example.crmapp.views.SignupView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            val viewModel = koinViewModel<SignupViewModel>()
            SignupView(viewModel, navController)
        }

        composable(Screen.LoginScreen.route) {
            val viewModel = koinViewModel<LoginViewModel>()
            val appState = koinInject<AppState>()
            LoginView(viewModel, navController, appState)
        }

        composable(Screen.HomeScreen.route) {
            val viewModel = koinViewModel<HomeScreenViewModel>()
            HomeView(viewModel, navController)
        }

        composable(Screen.ContactScreen.route) {
            val viewModel = koinViewModel<ContactScreenViewModel>()
            ContactView(viewModel, navController)
        }
    }
}