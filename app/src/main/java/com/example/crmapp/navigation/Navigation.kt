package com.example.crmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val appState = koinInject<AppState>()
    val isAuthenticated by appState.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if(isAuthenticated) Screen.HomeScreen.route else Screen.LoginScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            val viewModel = koinViewModel<SignupViewModel>()
            SignupView(viewModel, navController)
        }

        composable(Screen.LoginScreen.route) {
            val viewModel = koinViewModel<LoginViewModel>()
            LoginView(viewModel, navController)
        }

        composable(Screen.HomeScreen.route) {
            val viewModel = koinViewModel<HomeScreenViewModel>()
            val noteViewModel = koinViewModel<ContactScreenViewModel>()
            val appState = koinInject<AppState>()
            HomeView(
                appState = appState,
                navController = navController,
                viewModel = viewModel,
                noteViewModel = noteViewModel
            )
        }

        composable(Screen.ContactScreen.route) {
            val viewModel = koinViewModel<ContactScreenViewModel>()
            val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()
            val appState = koinInject<AppState>()
            ContactView(viewModel, homeScreenViewModel, navController, appState)
        }
    }
}