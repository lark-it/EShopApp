package com.example.eshopapp.data.mapper

import com.example.eshopapp.data.network.dto.CategoryDto
import com.example.eshopapp.data.network.dto.ProductDto
import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product
import kotlin.Int

fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        title = title,
        category = category,
        price = price,
        image = thumbnail
    )

fun CategoryDto.toDomain(): Category =
    Category(
        name = name,
        url = url
    )