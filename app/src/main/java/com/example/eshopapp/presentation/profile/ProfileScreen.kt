package com.example.eshopapp.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ProfileScreen() {
    Column(
        Modifier.fillMaxSize()
    ) {
        Text(
            "Профиль",
            textAlign = TextAlign.Center
        )
    }
}