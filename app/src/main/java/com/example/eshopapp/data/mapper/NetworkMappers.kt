package com.example.eshopapp.data.mapper

import com.example.eshopapp.data.network.dto.CategoryDto
import com.example.eshopapp.data.network.dto.ProductDto
import com.example.eshopapp.data.network.dto.ReviewsDto
import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.domain.model.Review
import com.example.eshopapp.presentation.category.CategoryCardUi

fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = (price * 100).toLong(),
        rating = rating,
        tags = tags,
        reviews = reviews.map { it.toDomain() },
        image = thumbnail
    )
fun ReviewsDto.toDomain(): Review =
    Review(
        rating = rating,
        comment = comment,
        name = reviewerName
    )
fun CategoryDto.toDomain(): Category =
    Category(
        name = name,
        slug = slug,
        imageUrl = null
    )
fun Category.toUi(): CategoryCardUi =
    CategoryCardUi(
        slug = slug,
        name = name,
        imageUrl = imageUrl
    )