package com.example.watchlinkapp.Entities.DAO.Movie

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchlinkapp.Entities.Model.Movie.Movie

@Dao
interface MovieDAO {
    @Query("select * from movies")
    fun getAllPagingData(): PagingSource<Int, Movie>

    @Query("select * from movies where movieId = :id")
    fun getMovie(id: Int): Movie

    @Insert
    suspend fun insert(vararg movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("DELETE FROM movies")
    suspend fun deleteAll()
}