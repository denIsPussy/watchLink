package com.example.watchlinkapp.Entities.Repository.Movie

import androidx.paging.PagingData
import com.example.watchlinkapp.API.Model.ReleaseYearRemote
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAll(): Flow<PagingData<Movie>>
    suspend fun getMovie(id: Int): Movie
    suspend fun getMoviesByDate(startDate: String, endDate: String): Flow<List<Movie>>
    suspend fun getReleaseYears(): Flow<ReleaseYearRemote>
    suspend fun insert(movie: Movie)
    suspend fun update(movie: Movie)
    suspend fun delete(movie: Movie)
}