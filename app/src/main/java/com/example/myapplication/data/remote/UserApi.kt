package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): UserDto
}