package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toUser
import com.example.myapplication.data.remote.UserApi
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
) : UserRepository {

    override suspend fun getUser(id: Int): User = api.getUser(id).toUser()
}