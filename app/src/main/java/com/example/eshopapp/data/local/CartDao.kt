package com.example.eshopapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Upsert
    suspend fun addToCart(item: CartItemEntity)

    @Query(
            "UPDATE cart_items " +
            "SET quantity = quantity + 1 " +
            "WHERE productId = :productId"
    )
    suspend fun increment(productId: Int)

    @Query(
        "UPDATE cart_items " +
            "SET quantity = quantity - 1 " +
            "WHERE productId = :productId "
    )
    suspend fun decrement(productId: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteById(productId: Int)

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemById(productId: Int): CartItemEntity?
}