package com.example.eshopapp.data.mapper

import com.example.eshopapp.data.local.CartItemEntity
import com.example.eshopapp.domain.model.Cart
import kotlin.Int

fun CartItemEntity.toDomain(): Cart =
    Cart(
        productId = productId,
        title = title,
        price = price,
        image = image,
        quantity = quantity
    )