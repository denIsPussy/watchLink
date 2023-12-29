package com.example.watchlinkapp.API.Mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.watchlinkapp.API.Model.toGenre
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.Database.AppDatabase
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeys
import com.example.watchlinkapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import com.example.watchlinkapp.Entities.Repository.Genre.OfflineGenreRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GenreRemoteMediator(
    private val service: MyServerService,
    private val dbGenreRepository: OfflineGenreRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, Genre>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Genre>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val genres = service.getGenres(page, state.config.pageSize).map { it.toGenre() }
            val endOfPaginationReached = genres.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.BOUQUET)
                    dbGenreRepository.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = genres.map {
                    RemoteKeys(
                        entityId = it.genreId!!,
                        type = RemoteKeyType.BOUQUET,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbGenreRepository.insertGenres(genres)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Genre>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { genre ->
                genre.genreId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.BOUQUET
                    )
                }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Genre>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { genre ->
                genre.genreId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.BOUQUET
                    )
                }
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Genre>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.genreId?.let { genreUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(genreUid, RemoteKeyType.BOUQUET)
            }
        }
    }
}