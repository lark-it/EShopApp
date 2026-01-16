package com.example.eshopapp.presentation.catalog

import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product


sealed interface CategoryUiState{
    data object Loading : CategoryUiState
    data class Content(val categories: List<Category>) : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}