package com.example.eshopapp.domain.model

data class Product (
    val id: Int,
    val title: String,
    val category: String,
    val price: Double,
    val image: String
)