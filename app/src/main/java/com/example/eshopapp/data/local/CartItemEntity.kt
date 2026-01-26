package com.example.eshopapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
)