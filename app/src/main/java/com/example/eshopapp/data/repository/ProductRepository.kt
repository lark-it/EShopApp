package com.example.eshopapp.data.repository

import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.network.ApiService
import com.example.eshopapp.domain.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun getProducts(): List<Product>{
        val response = api.getProducts()
        return response.products.map { it.toDomain() }
    }
    suspend fun getProductInfo(id: Int): Product{
        val product = api.getProductInfo(id)
        return product.toDomain()
    }

    suspend fun searchProducts(
        query: String,
        limit: Int
    ): List<Product>{
        val response = api.searchProducts(query, limit)
        return response.products.map { it.toDomain() }
    }
}
