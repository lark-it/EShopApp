package com.example.eshopapp.domain.model

data class Product (
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val rating: Double,
    val price: Double,
    val tags: List<String>,
    val image: String
)