package com.example.eshopapp.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "eshop_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCartDao(db: AppDatabase) = db.cartDao()

    @Provides
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteDao()
}