package com.example.watchlinkapp.Entities.Model.Movie

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef

data class MovieWithGenres(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<Genre>
)
