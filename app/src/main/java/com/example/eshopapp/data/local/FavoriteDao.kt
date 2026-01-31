package com.example.eshopapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_items")
    fun getFavorite(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(entity:FavoriteEntity)

    @Query("DELETE FROM favorite_items WHERE productId = :productId")
    suspend fun deleteFromFavorite(productId: Int)
}