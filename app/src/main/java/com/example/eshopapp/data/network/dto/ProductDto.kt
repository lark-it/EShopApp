package com.example.eshopapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val tags: List<String>,
    val reviews: List<ReviewsDto>,
    val thumbnail: String
)
@JsonClass(generateAdapter = true)
data class ReviewsDto(
    val rating: Int,
    val comment: String,
    val reviewerName: String
)