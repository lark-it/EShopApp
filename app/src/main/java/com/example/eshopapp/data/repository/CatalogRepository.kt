package com.example.eshopapp.data.repository

import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.network.ApiService
import com.example.eshopapp.domain.model.Category
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    val api: ApiService
){
    suspend fun getCategories(): List<Category>{
        val response = api.getCategories()
        return response.map {it.toDomain()}
    }
}