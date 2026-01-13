package com.example.eshopapp.domain.model

data class Product (
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val rating: Double,
    val price: Double,
    val tags: List<String>,
    val reviews: List<Review>,
    val image: String
)

data class Review(
    val rating: Int,
    val comment: String,
    val name: String
)