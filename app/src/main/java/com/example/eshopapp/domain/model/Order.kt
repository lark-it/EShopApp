package com.example.eshopapp.domain.model

data class Order(
    val id: Long = 0,
    val orderNumber: String,
    val createdAt: Long,
    val items: List<OrderItem>,
    val totalCount: Int,
    val totalPrice: Long,
    val addressText: String
    //статуса заказа не будет
)

data class OrderItem(
    val productId: Int,
    val title: String,
    val price: Long,
    val image: String,
    val quantity: Int
)