package com.example.eshopapp.presentation.cart

import com.example.eshopapp.domain.model.Cart
import com.example.eshopapp.domain.model.Product

data class CartItem(
    val product: Product,
    val quantity: Int
)

data class CartUiState(
    val items: List<Cart> = emptyList(),
    val totalCount: Int = 0,
    val totalPrice: Double = 0.0,
    val error: String? = null
)