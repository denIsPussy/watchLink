package com.example.watchlinkapp.Entities.Model.Genre

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.Entities.Model.MovieGenre.MovieGenreCrossRef

data class GenresWithMovies(
    @Embedded val genre: Genre,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "movieId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val movies: List<Movie>
)
