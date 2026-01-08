package com.example.eshopapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val name: String,
    val url: String
    //картинку придумаю позже
)