package com.example.crmapp.navigation

sealed class Screen(val route: String) {
    object LoginScreen: Screen("login_screen")
    object SignupScreen: Screen("signup_screen")
    object HomeScreen: Screen("home_screen")
    object ContactScreen: Screen("contact_screen/{contactId}") {
        fun createRoute(contactId: Int) = "contact_screen/$contactId"
    }
}