package com.example.watchlinkapp.Database.RemoteKeys.Repository

import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeys


interface RemoteKeyRepository {
    suspend fun getAllRemoteKeys(id: Int, type: RemoteKeyType): RemoteKeys?
    suspend fun createRemoteKeys(remoteKeys: List<RemoteKeys?>)
    suspend fun deleteRemoteKey(type: RemoteKeyType)
}