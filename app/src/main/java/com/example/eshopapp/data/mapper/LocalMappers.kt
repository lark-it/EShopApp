package com.example.eshopapp.data.mapper

import com.example.eshopapp.data.local.AddressEntity
import com.example.eshopapp.data.local.CartItemEntity
import com.example.eshopapp.data.local.OrderEntity
import com.example.eshopapp.data.local.OrderItemEntity
import com.example.eshopapp.data.local.OrderWithItems
import com.example.eshopapp.domain.model.Cart
import com.example.eshopapp.domain.model.Order
import com.example.eshopapp.domain.model.OrderItem
import com.example.eshopapp.presentation.profile.Address
import kotlin.Int

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

fun OrderWithItems.toDomain(): Order =
    Order(
        id = order.id,
        orderNumber = order.orderNumber,
        createdAt = order.createdAt,
        items = items.map { it.toDomain() },
        totalCount = order.totalCount,
        totalPrice = order.totalPrice,
        addressText = order.addressText
    )
fun OrderItemEntity.toDomain(): OrderItem =
    OrderItem(
        productId = productId,
        title = title,
        price = price,
        image = image,
        quantity = quantity
    )

fun Order.toEntity(): OrderEntity =
    OrderEntity(
        id = id,
        orderNumber = orderNumber,
        createdAt = createdAt,
        totalCount = totalCount,
        totalPrice = totalPrice,
        addressText = addressText
    )
fun OrderItem.toEntity(orderId: Long): OrderItemEntity =
    OrderItemEntity(
        productId = productId,
        orderId = orderId,
        title = title,
        price = price,
        image = image,
        quantity = quantity
    )