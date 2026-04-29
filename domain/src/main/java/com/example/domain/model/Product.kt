package com.example.domain.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val isFavorite: Boolean = false
)
