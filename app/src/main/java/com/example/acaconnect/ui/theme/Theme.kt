package com.example.acaconnect.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GoldDark,
    secondary = MaroonDark,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = MaroonPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFDE7E7), // Very soft maroon tint for backgrounds
    onPrimaryContainer = MaroonPrimary,
    secondary = GoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF4D6), // Soft gold tint for highlights
    onSecondaryContainer = Color(0xFF261900),
    background = SoftWhite,
    surface = Color.White,
    onBackground = DeepGrey,
    onSurface = DeepGrey
)

@Composable
fun ACAConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Using a darker color for status bar for better readability
            window.statusBarColor = MaroonSecondary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
