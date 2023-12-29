package com.example.watchlinkapp.Entities.Repository.Genre

import androidx.paging.PagingData
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun getAll(): Flow<PagingData<Genre>>
    suspend fun getGenre(id: Int): Genre
    suspend fun getAllGenres(): Flow<List<Genre>>
    suspend fun insert(genre: Genre)
    suspend fun update(genre: Genre)
    suspend fun delete(genre: Genre)
}