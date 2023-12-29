package com.example.watchlinkapp.API.Repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.watchlinkapp.API.Mediator.MovieRemoteMediator
import com.example.watchlinkapp.API.Model.ReleaseYearRemote
import com.example.watchlinkapp.API.Model.toMovie
import com.example.watchlinkapp.API.Model.toMovieRemote
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.Database.AppContainer
import com.example.watchlinkapp.Database.AppDatabase
import com.example.watchlinkapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.Entities.Repository.Movie.MovieRepository
import com.example.watchlinkapp.Entities.Repository.Movie.OfflineMovieRepository
import com.example.watchlinkapp.Entities.Repository.MovieGenre.OfflineMovieGenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RestMovieRepository(
    private val service: MyServerService,
    private val dbMovieRepository: OfflineMovieRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val movieGenreRepository: RestMovieGenreRepository,
    private val dbMovieGenreRepository: OfflineMovieGenreRepository,
    private val database: AppDatabase
) : MovieRepository {
    override fun getAll(): Flow<PagingData<Movie>> {
        Log.d(RestMovieRepository::class.simpleName, "Get Movies")
        val pagingSourceFactory = { dbMovieRepository.getAllMoviesPagingSource() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT_MOVIES,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(
                service,
                dbMovieRepository,
                dbRemoteKeyRepository,
                movieGenreRepository,
                dbMovieGenreRepository,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getMovie(id: Int): Movie {
        return service.getMovie(id).toMovie()
    }

    override suspend fun getMoviesByDate(startDate: String, endDate: String): Flow<List<Movie>> {
        return flowOf(service.getMoviesByDate(startDate, endDate).map { it.toMovie() })
    }

    override suspend fun getReleaseYears(): Flow<ReleaseYearRemote> {
        return flowOf(service.getReleaseYears())
    }

    override suspend fun insert(movie: Movie) {
        service.createMovie(movie.toMovieRemote()).toMovie()
    }

    override suspend fun update(movie: Movie) {
        movie.movieId?.let { service.updateMovie(it, movie.toMovieRemote()).toMovie() }
    }

    override suspend fun delete(movie: Movie) {
        movie.movieId?.let { service.deleteMovie(it).toMovie() }
    }
}