package com.example.network.remote.dto

data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    val name: UserNameDto,
    val phone: String,
    val address: UserAddressDto
)
