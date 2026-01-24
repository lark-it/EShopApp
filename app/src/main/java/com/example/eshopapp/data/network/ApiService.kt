package com.example.eshopapp.data.network

import com.example.eshopapp.data.network.dto.CategoryDto
import com.example.eshopapp.data.network.dto.ProductDto
import com.example.eshopapp.data.network.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(): ProductsResponseDto

    @GET("products/{id}")
    suspend fun getProductInfo(@Path("id") id: Int): ProductDto

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("products/category/{slug}")
    suspend fun getCategoryProducts(@Path("slug") slug: String): ProductsResponseDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int = 0
    ): ProductsResponseDto
}