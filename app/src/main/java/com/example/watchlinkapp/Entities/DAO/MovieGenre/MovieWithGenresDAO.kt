package com.example.watchlinkapp.Entities.DAO.MovieGenre

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef

@Dao
interface MovieWithGenresDAO {
    @Query("select * from moviegenrecrossref")
    fun getAll(): List<MovieGenreCrossRef>

    @Insert
    suspend fun insert(vararg movie: MovieGenreCrossRef)

    @Update
    suspend fun update(movie: MovieGenreCrossRef)

    @Delete
    suspend fun delete(movie: MovieGenreCrossRef)

    @Query("DELETE FROM moviegenrecrossref")
    suspend fun deleteAll()
}