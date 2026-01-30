package com.example.eshopapp.data.local

import androidx.room.Entity

@Entity(tableName = "favorite_items")
data class FavoriteEntity(
    val productId: Int
)