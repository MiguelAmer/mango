package com.example.myapplication.data.repository

import com.example.myapplication.data.local.FavoriteDao
import com.example.myapplication.data.mapper.toProduct
import com.example.myapplication.data.mapper.toProductEntity
import com.example.myapplication.data.mapper.toProductList
import com.example.myapplication.data.remote.ProductApi
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val dao: FavoriteDao
) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { it.toProduct() }
    }

    override fun getFavorites(): Flow<List<Product>> {
        return dao.getFavorites().toProductList()
    }

    override suspend fun toggleFavorite(product: Product) {
        if (product.isFavorite) {
            dao.deleteFavorite(product.toProductEntity())
        } else {
            dao.insertFavorite(product.toProductEntity())
        }
    }

    override fun getFavoriteCount(): Flow<Int> {
        return dao.getFavoriteCount()
    }
}


