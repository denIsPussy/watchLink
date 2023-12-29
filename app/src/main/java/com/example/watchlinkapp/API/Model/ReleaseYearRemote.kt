package com.example.watchlinkapp.API.Model

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseYearRemote(
    val releaseYears: List<Int> = listOf()
)