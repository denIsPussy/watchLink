package com.example.watchlinkapp.API.Repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.watchlinkapp.API.Mediator.GenreRemoteMediator
import com.example.watchlinkapp.API.Model.toGenre
import com.example.watchlinkapp.API.Model.toGenreRemote
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.Database.AppContainer
import com.example.watchlinkapp.Database.AppDatabase
import com.example.watchlinkapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import com.example.watchlinkapp.Entities.Repository.Genre.GenreRepository
import com.example.watchlinkapp.Entities.Repository.Genre.OfflineGenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RestGenreRepository(
    private val service: MyServerService,
    private val dbGenreRepository: OfflineGenreRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : GenreRepository {
    override fun getAll(): Flow<PagingData<Genre>> {
        Log.d(RestGenreRepository::class.simpleName, "Get Genres")
        val pagingSourceFactory = { dbGenreRepository.getAllGenresPagingSource() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT_GENRES,
                enablePlaceholders = false
            ),
            remoteMediator = GenreRemoteMediator(
                service,
                dbGenreRepository,
                dbRemoteKeyRepository,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getGenre(id: Int): Genre {
        return service.getGenre(id).toGenre()
    }

    override suspend fun insert(genre: Genre) {
        service.createGenre(genre.toGenreRemote()).toGenre()
    }

    override suspend fun update(genre: Genre) {
        genre.genreId?.let { service.updateGenre(it, genre.toGenreRemote()).toGenre() }
    }

    override suspend fun delete(genre: Genre) {
        genre.genreId?.let { service.deleteGenre(it).toGenre() }
    }

    override suspend fun getAllGenres(): Flow<List<Genre>> {
        return flowOf(service.getAllGenres().map { it.toGenre() })
    }
}