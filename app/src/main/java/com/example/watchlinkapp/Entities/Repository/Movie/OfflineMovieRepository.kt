package com.example.watchlinkapp.Entities.Repository.Movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.watchlinkapp.API.Model.ReleaseYearRemote
import com.example.watchlinkapp.Database.AppContainer
import com.example.watchlinkapp.Entities.DAO.Movie.MovieDAO
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import kotlinx.coroutines.flow.Flow

class OfflineMovieRepository(private val movieDAO: MovieDAO) : MovieRepository {
    override fun getAll(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT_MOVIES,
            enablePlaceholders = false
        ),
        pagingSourceFactory = movieDAO::getAllPagingData
    ).flow

    override suspend fun getMovie(id: Int): Movie = movieDAO.getMovie(id)
    override suspend fun getMoviesByDate(startDate: String, endDate: String): Flow<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReleaseYears(): Flow<ReleaseYearRemote> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(movie: Movie) = movieDAO.insert(movie)

    override suspend fun update(movie: Movie) = movieDAO.update(movie)

    override suspend fun delete(movie: Movie) = movieDAO.delete(movie)
    suspend fun deleteAll() = movieDAO.deleteAll()
    fun getAllMoviesPagingSource(): PagingSource<Int, Movie> = movieDAO.getAllPagingData()
    suspend fun insertMovies(movies: List<Movie>) =
        movieDAO.insert(*movies.toTypedArray())
}