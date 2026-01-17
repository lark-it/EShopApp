package com.example.eshopapp.data.repository

import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.network.ApiService
import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    val api: ApiService
){
    suspend fun getCategories(): List<Category>{
        val response = api.getCategories()
        return response.map {it.toDomain()}
    }

    suspend fun getCategoryProducts(category: String): List<Product>{
        val response = api.getCategoryProducts(category)
        return response.products.map {it.toDomain()}
    }
}