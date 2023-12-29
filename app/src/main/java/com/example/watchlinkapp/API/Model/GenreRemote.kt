package com.example.watchlinkapp.API.Model

import com.example.watchlinkapp.Entities.Model.Genre.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreRemote(
    val id: Int? = 0,
    val name: String = ""
)

fun GenreRemote.toGenre(): Genre = Genre(
    id,
    name
)

fun Genre.toGenreRemote(): GenreRemote = GenreRemote(
    genreId,
    name
)