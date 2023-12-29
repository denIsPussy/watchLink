package com.example.watchlinkapp.API.Model

import kotlinx.serialization.Serializable

@Serializable
data class MovieCountByGenreRemote(
    val genreName: String,
    val movieCount: Int
)