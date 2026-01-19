package com.example.eshopapp.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val name: String,
    val slug: String
    //картинку придумаю позже
)