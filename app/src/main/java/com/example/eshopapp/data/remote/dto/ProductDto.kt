package com.example.eshopapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val title: String,
    val category: String,
    val price: Double,
    val image: String,
    /*
    потом сделать:
    val description: String,
    val rating: Double,
    val brand: String
    tags, reviews
     */
)