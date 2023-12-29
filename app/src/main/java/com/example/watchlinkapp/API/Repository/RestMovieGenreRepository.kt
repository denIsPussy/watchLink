package com.example.watchlinkapp.API.Repository

import android.util.Log
import com.example.watchlinkapp.API.Model.MovieGenreCountRemote
import com.example.watchlinkapp.API.Model.MovieGenreCrossRef
import com.example.watchlinkapp.API.Model.MovieGenreCrossRefRemote
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef
import com.example.watchlinkapp.Entities.Repository.MovieGenre.MovieGenreRepository
import com.example.watchlinkapp.Entities.Repository.MovieGenre.OfflineMovieGenreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class RestMovieGenreRepository(
    private val service: MyServerService,
    private val dbMovieGenreRepository: OfflineMovieGenreRepository,
) : MovieGenreRepository {
    override suspend fun getAll(): List<MovieGenreCrossRef> {
        Log.d(RestMovieGenreRepository::class.simpleName, "Get MovieGenres")
        val localMovieGenres = withContext(Dispatchers.IO) {
            dbMovieGenreRepository.getAll().toMutableList()
        }
        val serverMovieGenres = service.getMoviesGenres().map { it.MovieGenreCrossRef() }
        val toDelete = localMovieGenres.filterNot { serverMovieGenres.contains(it) }
        toDelete.forEach { dbMovieGenreRepository.delete(it) }

        localMovieGenres.removeAll(toDelete)
        val toAdd = serverMovieGenres.filterNot { localMovieGenres.contains(it) }
        toAdd.forEach { dbMovieGenreRepository.insert(it) }

        localMovieGenres.addAll(toAdd)

        return localMovieGenres
    }

    override suspend fun insert(movie: MovieGenreCrossRef) {
        service.createMovieGenre(movie.MovieGenreCrossRefRemote())
    }

    override suspend fun delete(movie: MovieGenreCrossRef) {
        service.deleteMovieGenre(movie.movieId)
    }

    override suspend fun getCountMoviesByGenre(): Flow<List<MovieGenreCountRemote>> {
        return flowOf(service.getMovieCountByGenre())
    }
}