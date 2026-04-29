package com.example.domain.model

data class User(
    val id: Int,
    val email: String,
    val username: String,
    val name: UserName,
    val phone: String,
    val address: UserAddress
)
