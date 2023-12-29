package com.example.watchlinkapp.API.Mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.watchlinkapp.API.Model.toMovie
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.API.Repository.RestMovieGenreRepository
import com.example.watchlinkapp.Database.AppDatabase
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.watchlinkapp.Database.RemoteKeys.Model.RemoteKeys
import com.example.watchlinkapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.Entities.Repository.Movie.OfflineMovieRepository
import com.example.watchlinkapp.Entities.Repository.MovieGenre.OfflineMovieGenreRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val service: MyServerService,
    private val dbMovieRepository: OfflineMovieRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val movieGenreRestRepository: RestMovieGenreRepository,
    private val dbMovieGenreRepository: OfflineMovieGenreRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, Movie>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
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
            val movies = service.getMovies(page, state.config.pageSize).map { it.toMovie() }
            val endOfPaginationReached = movies.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.BOUQUET)
                    dbMovieRepository.deleteAll()

                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    RemoteKeys(
                        entityId = it.movieId!!,
                        type = RemoteKeyType.BOUQUET,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbMovieRepository.insertMovies(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                movie.movieId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.BOUQUET
                    )
                }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                movie.movieId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.BOUQUET
                    )
                }
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieId?.let { movieUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(movieUid, RemoteKeyType.BOUQUET)
            }
        }
    }
}