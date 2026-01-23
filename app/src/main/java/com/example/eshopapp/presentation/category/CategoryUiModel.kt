package com.example.eshopapp.presentation.category

import com.example.eshopapp.domain.model.Product


sealed interface CategoryUiState{
    data object Loading : CategoryUiState
    data class Content(val categories: List<CategoryCardUi>) : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}

sealed interface CatalogUiState{
    data object Loading : CatalogUiState
    data class Content(val products: List<Product>) : CatalogUiState
    data class Error(val message: String) : CatalogUiState
}