package com.example.acaconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.acaconnect.ui.MainScreen
import com.example.acaconnect.ui.theme.ACAConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACAConnectTheme {
                MainScreen()
            }
        }
    }
}
