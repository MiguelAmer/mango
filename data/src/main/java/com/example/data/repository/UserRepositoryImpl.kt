package com.example.data.repository

import com.example.data.mapper.toUser
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.network.remote.UserApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
) : UserRepository {

    override suspend fun getUser(id: Int): User = api.getUser(id).toUser()
}