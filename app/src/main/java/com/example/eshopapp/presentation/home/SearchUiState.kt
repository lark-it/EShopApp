package com.example.eshopapp.presentation.home

import com.example.eshopapp.domain.model.Product

data class SearchUiState(
    val query: String,
    val expanded: Boolean,
    val suggestions: List<Product>,
    val isLoading: Boolean,
    val error: String?
)

data class SearchResultsUiState(
    val query: String,
    val items: List<Product>,
    val isLoading: Boolean,
    val error: String?
)