package com.example.data.mapper

import com.example.domain.model.User
import com.example.domain.model.UserAddress
import com.example.domain.model.UserName
import com.example.network.remote.dto.UserAddressDto
import com.example.network.remote.dto.UserDto
import com.example.network.remote.dto.UserNameDto

fun UserDto.toUser() =
    User(
        id,
        email,
        username,
        name.toUserName(),
        phone,
        address.toUserAddress()
    )

fun UserNameDto.toUserName() =
    UserName(
        firstname,
        lastname
    )

fun UserAddressDto.toUserAddress() =
    UserAddress(
        streetName = street,
        city = city,
        zipCode = zipcode
    )