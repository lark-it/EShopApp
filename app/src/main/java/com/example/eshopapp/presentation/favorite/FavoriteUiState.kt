package com.example.eshopapp.presentation.favorite

import com.example.eshopapp.domain.model.Product

data class FavoriteUiState(
    val items: List<Product> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
