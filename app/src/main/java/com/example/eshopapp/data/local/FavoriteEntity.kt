package com.example.eshopapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteEntity(
    @PrimaryKey
    val productId: Int
)