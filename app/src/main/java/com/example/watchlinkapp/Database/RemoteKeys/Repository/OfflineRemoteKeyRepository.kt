package com.example.watchlinkapp.Database.RemoteKeys.Repository

import com.example.watchlinkapp.Database.RemoteKeys.DAO.RemoteKeysDAO
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeys

class OfflineRemoteKeyRepository(private val remoteKeysDao: RemoteKeysDAO) : RemoteKeyRepository {
    override suspend fun getAllRemoteKeys(id: Int, type: RemoteKeyType) =
        remoteKeysDao.getRemoteKeys(id, type)

    override suspend fun createRemoteKeys(remoteKeys: List<RemoteKeys?>) =
        remoteKeysDao.insertAll(remoteKeys)

    override suspend fun deleteRemoteKey(type: RemoteKeyType) =
        remoteKeysDao.clearRemoteKeys(type)
}