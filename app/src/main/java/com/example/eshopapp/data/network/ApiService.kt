package com.example.eshopapp.data.network

import com.example.eshopapp.data.network.dto.CategoryDto
import com.example.eshopapp.data.network.dto.ProductDto
import com.example.eshopapp.data.network.dto.ProductsResponseDto
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): ProductsResponseDto

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>
}