package com.example.watchlinkapp.Entities.Repository.Genre

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.watchlinkapp.Database.AppContainer
import com.example.watchlinkapp.Entities.DAO.Genre.GenreDAO
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class OfflineGenreRepository(private val genreDAO: GenreDAO) : GenreRepository {
    override fun getAll(): Flow<PagingData<Genre>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT_GENRES,
            enablePlaceholders = false
        ),
        pagingSourceFactory = genreDAO::getAllPagingData
    ).flow

    override suspend fun getGenre(id: Int): Genre = genreDAO.getGenre(id)
    override suspend fun getAllGenres(): Flow<List<Genre>> {
        return flowOf(genreDAO.getAll())
    }

    override suspend fun insert(genre: Genre) = genreDAO.insert(genre)
    override suspend fun update(genre: Genre) = genreDAO.update(genre)
    override suspend fun delete(genre: Genre) = genreDAO.delete(genre)
    suspend fun deleteAll() = genreDAO.deleteAll()
    fun getAllGenresPagingSource(): PagingSource<Int, Genre> = genreDAO.getAllPagingData()
    suspend fun insertGenres(genres: List<Genre>) =
        genreDAO.insert(*genres.toTypedArray())
}