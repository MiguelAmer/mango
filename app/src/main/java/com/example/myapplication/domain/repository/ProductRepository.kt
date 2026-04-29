package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    fun getFavorites(): Flow<List<Product>>
    suspend fun toggleFavorite(product: Product)
    fun getFavoriteCount(): Flow<Int>
}
