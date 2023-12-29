package com.example.watchlinkapp.ComposeUI.Movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.example.watchlinkapp.ComposeUI.Network.NetworkViewModel
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.Entities.Repository.Genre.GenreRepository
import com.example.watchlinkapp.Entities.Repository.Movie.MovieRepository
import com.example.watchlinkapp.Entities.Repository.MovieGenre.MovieGenreRepository
import kotlinx.coroutines.flow.first

class MovieViewModel(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val genresRepository: GenreRepository,
    private val movieGenresRepository: MovieGenreRepository
) : NetworkViewModel() {
    var movieUiState by mutableStateOf(MovieUiState())
        private set
    private val movieUid: Int = checkNotNull(savedStateHandle["id"])

    init {
        runInScope(
            actionSuccess = {
                if (movieUid > 0) {
                    val movie = movieRepository.getMovie(movieUid)
                    val genreIdList =
                        movieGenresRepository.getAll().filter { it.movieId == movieUid }
                            .map { it.genreId }.toList()
                    val genreList =
                        genresRepository.getAllGenres().first().filter { it.genreId in genreIdList }
                    movieUiState = Pair(movie, genreList).toUiState()
                }
            },
            actionError = {
                movieUiState = MovieUiState()
            }
        )
    }
}

data class MovieUiState(
    val movieDetails: MovieDetails = MovieDetails()
)

data class MovieDetails(
    val movie: Movie? = null,
    val genres: List<Genre> = listOf()
)

fun Pair<Movie, List<Genre>>.toDetails(): MovieDetails = MovieDetails(
    movie = this.first,
    genres = this.second
)

fun Pair<Movie, List<Genre>>.toUiState(): MovieUiState = MovieUiState(
    movieDetails = this.toDetails()
)

fun List<Genre>.toCustomString(): String {
    var genresString: String = ""
    this.forEach { genre ->
        genresString += "${genre.name}, "
    }
    return genresString.dropLast(2)
}
