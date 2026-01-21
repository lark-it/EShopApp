package com.example.eshopapp.data.repository

import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.mapper.toUi
import com.example.eshopapp.data.network.ApiService
import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.catalog.CategoryCardUi
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
}
