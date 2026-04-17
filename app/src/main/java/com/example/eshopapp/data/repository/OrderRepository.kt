package com.example.eshopapp.data.repository

import com.example.eshopapp.data.local.OrderDao
import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.mapper.toEntity
import com.example.eshopapp.domain.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val dao: OrderDao
){
    fun observeOrders(): Flow<List<Order>>{
        return dao.getAllOrders().map { list -> list.map { it.toDomain() } }
    }
    suspend fun insertOrder(order: Order){
        dao.insertOrderWithItems(
            order = order.toEntity(),
            items = order.items.map { it.toEntity(order.id) }
        )
    }
}