package com.example.watchlinkapp.API.Model

import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef
import kotlinx.serialization.Serializable

@Serializable
data class MovieGenreCrossRefRemote(
    val id: Int? = null,
    val movieId: Int = 0,
    val genreId: Int = 0,
)

fun MovieGenreCrossRefRemote.MovieGenreCrossRef(): MovieGenreCrossRef = MovieGenreCrossRef(
    movieId,
    genreId
)

fun MovieGenreCrossRef.MovieGenreCrossRefRemote(): MovieGenreCrossRefRemote =
    MovieGenreCrossRefRemote(
        null,
        movieId,
        genreId
    )