package com.example.eshopapp.data.repository

import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.network.ApiService
import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.domain.model.Product
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    val api: ApiService
){
    suspend fun getCategories(): List<Category>{
        return api.getCategories().map { it.toDomain() }
    }

    suspend fun getCategoryProducts(category: String): List<Product>{
        val response = api.getCategoryProducts(category)
        return response.products.map {it.toDomain()}
    }

    suspend fun getTopProductInCategory(slug: String): Product{
        val products = api.getCategoryProducts(slug).products
        val topProductDto = products.maxBy { it.rating }
        return topProductDto.toDomain()
    }

    suspend fun getCategoriesWithImages(
        limit: Int? = null,
    ): List<Category> = coroutineScope {
        val allCategories = getCategories()
        val limitCategories = limit?.let { allCategories.take(it) } ?: allCategories

        val semaphore = Semaphore(permits = 3)

        limitCategories.map { category ->
            async {
                semaphore.withPermit {
                    val imageUrl = runCatching {
                        getTopProductInCategory(category.slug).image
                    }.getOrNull()
                    category.copy(imageUrl = imageUrl)
                }
            }
        }.awaitAll()
    }
}