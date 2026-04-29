package com.example.domain.usecase

import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductsWithFavoritesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = flow {
        val baseProducts = repository.getProducts()

        val combinedFlow = repository.getFavorites().map { favorites ->
            val favoriteIds = favorites.map { it.id }.toSet()

            baseProducts.map { product ->
                product.copy(isFavorite = favoriteIds.contains(product.id))
            }
        }

        emitAll(combinedFlow)
    }
}