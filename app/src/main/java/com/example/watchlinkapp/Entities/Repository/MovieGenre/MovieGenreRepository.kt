package com.example.watchlinkapp.Entities.Repository.MovieGenre

import com.example.watchlinkapp.API.Model.MovieGenreCountRemote
import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef
import kotlinx.coroutines.flow.Flow

interface MovieGenreRepository {
    suspend fun getAll(): List<MovieGenreCrossRef>
    suspend fun insert(movie: MovieGenreCrossRef)
    suspend fun delete(movie: MovieGenreCrossRef)
    suspend fun update(movie: MovieGenreCrossRef)
    suspend fun getCountMoviesByGenre(): Flow<List<MovieGenreCountRemote>>
}