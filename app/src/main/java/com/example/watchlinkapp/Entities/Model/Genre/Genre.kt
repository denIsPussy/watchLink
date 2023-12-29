package com.example.watchlinkapp.Entities.Model.Genre

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey(autoGenerate = true)
    val genreId: Int?,
    @ColumnInfo(name = "genre_name")
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Genre
        return genreId == other.genreId
    }

    override fun hashCode(): Int {
        return genreId ?: -1
    }
}
