package com.example.watchlinkapp.Entities.Repository.User

import com.example.watchlinkapp.Entities.Model.User.User

interface UserRepository {
    suspend fun getAll(): List<User>
    suspend fun getUser(userName: String): User
    suspend fun insert(user: User)
    suspend fun update(user: User)
    suspend fun delete(user: User)
}