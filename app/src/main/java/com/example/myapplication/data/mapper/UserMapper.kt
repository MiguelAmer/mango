package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.UserAddressDto
import com.example.myapplication.data.remote.dto.UserDto
import com.example.myapplication.data.remote.dto.UserNameDto
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserAddress
import com.example.myapplication.domain.model.UserName

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