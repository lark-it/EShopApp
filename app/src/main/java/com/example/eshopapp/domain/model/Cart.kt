package com.example.eshopapp.domain.model

data class Cart (
    val productId: Int,
    val title: String,
    val price: Long,
    val image: String,
    val quantity: Int
)