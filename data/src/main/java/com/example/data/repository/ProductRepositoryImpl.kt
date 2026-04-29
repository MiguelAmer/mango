package com.example.data.repository

import com.example.data.mapper.toProduct
import com.example.data.mapper.toProductEntity
import com.example.data.mapper.toProductList
import com.example.database.local.FavoriteDao
import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import com.example.network.remote.ProductApi
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


