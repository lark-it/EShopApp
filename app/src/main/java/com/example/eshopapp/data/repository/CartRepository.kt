package com.example.eshopapp.data.repository

import com.example.eshopapp.data.local.CartDao
import com.example.eshopapp.data.local.CartItemEntity
import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.domain.model.Cart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val dao: CartDao
) {
    fun observeCartItems(): Flow<List<Cart>> =
        dao.getCartItems().map { list -> list.map { it.toDomain() } }

    suspend fun addToCart(item: CartItemEntity) = dao.addToCart(item)

    suspend fun increment(productId: Int) = dao.increment(productId)

    suspend fun decrement(productId: Int) = dao.decrement(productId)

    suspend fun deleteById(productId: Int) = dao.deleteById(productId)
}