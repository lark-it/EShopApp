package com.example.eshopapp.data.mapper

import com.example.eshopapp.data.local.AddressEntity
import com.example.eshopapp.data.local.CartItemEntity
import com.example.eshopapp.domain.model.Cart
import com.example.eshopapp.presentation.profile.Address
import kotlin.Int
import kotlin.String

fun CartItemEntity.toDomain(): Cart =
    Cart(
        productId = productId,
        title = title,
        price = price,
        image = image,
        quantity = quantity
    )

fun AddressEntity.toDomain(): Address =
    Address(
        id = addressId,
        street = street,
        apartment = apartment,
        entrance = entrance,
        floor = floor,
        comment = comment
    )
fun Address.toEntity(): AddressEntity =
    AddressEntity(
        addressId = id ?: 0,
        street = street,
        apartment = apartment,
        entrance = entrance,
        floor = floor,
        comment = comment
    )