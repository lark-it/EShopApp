package com.example.eshopapp.data.repository

import com.example.eshopapp.data.local.FavoriteDao
import com.example.eshopapp.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val dao: FavoriteDao
){
    fun favoriteProducts(): Flow<Set<Int>> =
        dao.getFavorite()
            .map { list -> list.map{ it.productId }.toSet() }

}