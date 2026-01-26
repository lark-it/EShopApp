package com.example.eshopapp.presentation.cart

import com.example.eshopapp.domain.model.Cart
import com.example.eshopapp.domain.model.Product

data class CartItem(
    val product: Product,
    val quantity: Int
)

data class CartUiState(
    val items: List<Cart> = emptyList(),
    val totalCount: Int,
    val totalPrice: Double,
    val error: String? = null
)