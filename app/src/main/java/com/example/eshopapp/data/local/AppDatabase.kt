package com.example.eshopapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CartItemEntity::class,
        FavoriteEntity::class,
        AddressEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun addressDao(): AddressDao
    abstract fun orderDao(): OrderDao
}