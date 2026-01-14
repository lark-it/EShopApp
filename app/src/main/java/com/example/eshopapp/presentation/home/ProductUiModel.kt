package com.example.eshopapp.presentation.home

import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product

sealed interface HomeUiState{
    data object Loading : HomeUiState
    data class Content(
        val products: List<Product>,
        val categories: List<Category>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface ProductUiState{
    data object Loading : ProductUiState
    data class Content(val product: Product) : ProductUiState
    data class Error(val message: String) : ProductUiState
}