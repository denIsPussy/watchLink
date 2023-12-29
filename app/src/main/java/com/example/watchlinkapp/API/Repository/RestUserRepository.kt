package com.example.watchlinkapp.API.Repository

import android.util.Log
import com.example.watchlinkapp.API.Model.toUser
import com.example.watchlinkapp.API.Model.toUserRemote
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.Entities.Model.User.User
import com.example.watchlinkapp.Entities.Repository.User.OfflineUserRepository
import com.example.watchlinkapp.Entities.Repository.User.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestUserRepository(
    private val service: MyServerService,
    private val dbUserRepository: OfflineUserRepository,
) : UserRepository {
    override suspend fun getAll(): List<User> {
        Log.d(RestUserRepository::class.simpleName, "Get Users")
        var existUsersList = listOf<User>()
        withContext(Dispatchers.IO) {
            existUsersList = dbUserRepository.getAll()
        }
        var existUsers = existUsersList.associateBy { it.userId }.toMutableMap()
        var remoteUsers = service.getUsers()
        remoteUsers
            .map { it.toUser() }
            .forEach { user ->
                val existUser = existUsers[user.userId!!]
                if (existUser == null) {
                    dbUserRepository.insert(user)
                } else if (existUser != user) {
                    dbUserRepository.update(user)
                }
                existUsers[user.userId] = user
            }

        return service.getUsers().map { it.toUser() }
    }

    override suspend fun getUser(userName: String): User {
        return service.getUser(userName)[0].toUser()
    }

    override suspend fun insert(user: User) {
        withContext(Dispatchers.IO) {
            dbUserRepository.insert(user)
            val insertedUser: User = dbUserRepository.getUser(user.userName)
            service.createUser(insertedUser.toUserRemote())
        }
    }

    override suspend fun update(user: User) {
        service.updateUser(user.userId!!, user.toUserRemote())
    }

    override suspend fun delete(user: User) {
        service.deleteUser(user.userId!!)
    }
}