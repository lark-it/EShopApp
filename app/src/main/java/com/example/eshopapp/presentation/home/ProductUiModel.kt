package com.example.eshopapp.presentation.home

import com.example.eshopapp.domain.model.Product

sealed interface HomeUiState{
    data object Loading : HomeUiState
    data class Content(val products: List<Product>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface ProductUiState{
    data object Loading : ProductUiState
    data class Content(val product: Product) : ProductUiState
    data class Error(val message: String) : ProductUiState
}