package com.example.data.mapper

import com.example.database.local.entities.ProductEntity
import com.example.domain.model.Product
import com.example.network.remote.dto.ProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        image = image,
        isFavorite = false
    )
}

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        image = image,
        isFavorite = true
    )
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        image = image
    )
}

fun Flow<List<ProductEntity>>.toProductList(): Flow<List<Product>> {
    return map { list -> list.map { it.toProduct() } }
}
