package com.example.acaconnect.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Login : Screen("login")
    object Home : Screen("home", "ACA Folders", Icons.Default.Home)
    object Tasks : Screen("tasks", "Tasks", Icons.Default.DateRange)
    object Mentors : Screen("mentors", "Mentors", Icons.Default.Star)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Tasks,
    Screen.Mentors,
    Screen.Profile
)
