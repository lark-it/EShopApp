package com.example.eshopapp.presentation.home

import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product

data class HomeUiState(
    val products: List<Product>,
    val productsLoading: Boolean,
    val productsError: String?,

    val categories: List<Category>,
    val categoriesLoading: Boolean,
    val categoriesError: String?
)

sealed interface ProductUiState{
    data object Loading : ProductUiState
    data class Content(val product: Product) : ProductUiState
    data class Error(val message: String) : ProductUiState
}