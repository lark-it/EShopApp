package com.example.eshopapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_items")
    fun getFavorite(): Flow<List<FavoriteEntity>>

    @Upsert
    suspend fun addToFavorite(productId: Int)

    @Delete
    suspend fun deleteFromFavorite(productId: Int)
}