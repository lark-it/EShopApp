package com.example.eshopapp.presentation.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CartScreen() {
    Column(
        Modifier.fillMaxSize()
    ) {
        Text(
            "Корзина",
            textAlign = TextAlign.Center
        )
    }
}