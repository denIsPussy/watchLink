package com.example.watchlinkapp.ComposeUI.Movie

import androidx.paging.PagingData
import com.example.watchlinkapp.API.Model.MovieGenreCountRemote
import com.example.watchlinkapp.API.Model.ReleaseYearRemote
import com.example.watchlinkapp.ComposeUI.Network.NetworkViewModel
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef
import com.example.watchlinkapp.Entities.Repository.Genre.GenreRepository
import com.example.watchlinkapp.Entities.Repository.Movie.MovieRepository
import com.example.watchlinkapp.Entities.Repository.MovieGenre.MovieGenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class MovieCatalogViewModel(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository,
    private val movieGenreRepository: MovieGenreRepository,
) : NetworkViewModel() {
    var movieListUiState: Flow<PagingData<Movie>> = movieRepository.getAll()
    var genreListUiState: Flow<PagingData<Genre>> = genreRepository.getAll()
    var movieGenreListUiState: List<MovieGenreCrossRef> = emptyList()
    private val _movieByDateListUiState = MutableStateFlow<List<Movie>>(emptyList())
    val movieByDateListUiState: Flow<List<Movie>> = _movieByDateListUiState.asStateFlow()
    private val _releaseYearListUiState = MutableStateFlow<ReleaseYearRemote>(ReleaseYearRemote())
    val releaseYearListUiState: Flow<ReleaseYearRemote> = _releaseYearListUiState.asStateFlow()
    var genreMovieCountListUiState: List<MovieGenreCountRemote> = emptyList()

    fun collectMoviesByDate(startDate: String, endDate: String) {
        runInScope(
            actionSuccess = {
                _movieByDateListUiState.value =
                    movieRepository.getMoviesByDate(startDate, endDate).first()
            },
            actionError = {
                _movieByDateListUiState.value = emptyList()
            }
        )
    }

    init {
        loadMovieGenre()
        loadReleaseYears()
        loadGenreMovieCount()
    }

    private fun loadMovieGenre() {
        runInScope(
            actionSuccess = {
                movieGenreListUiState = movieGenreRepository.getAll()
            },
            actionError = {
                movieGenreListUiState = emptyList()
            }
        )
    }

    private fun loadReleaseYears() {
        runInScope(
            actionSuccess = {
                movieRepository.getReleaseYears().collect { releaseYears ->
                    _releaseYearListUiState.value = releaseYears
                }
            },
            actionError = {
                _releaseYearListUiState.value = ReleaseYearRemote()
            }
        )
    }

    fun loadGenreMovieCount() {
        runInScope(
            actionSuccess = {
                genreMovieCountListUiState = movieGenreRepository.getCountMoviesByGenre().first()
            },
            actionError = {
                genreMovieCountListUiState = emptyList()
            }
        )
    }

    fun containsMovieInGenre(movieId: Int, genreId: Int): Boolean {
        return movieGenreListUiState.find { it.movieId == movieId && it.genreId == genreId } != null
    }
}