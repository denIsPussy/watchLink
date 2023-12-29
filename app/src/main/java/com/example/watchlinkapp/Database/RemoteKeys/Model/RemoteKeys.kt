package com.example.watchlinkapp.Database.RemoteKeys.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.watchlinkapp.Entities.Model.Genre.Genre
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.Entities.Model.User.User

enum class RemoteKeyType(private val type: String) {
    BOUQUET(Movie::class.simpleName ?: "Movie"),
    ORDER(Genre::class.simpleName ?: "Genre"),
    USER(User::class.simpleName ?: "User");

    @TypeConverter
    fun toRemoteKeyType(value: String) = RemoteKeyType.values().first { it.type == value }

    @TypeConverter
    fun fromRemoteKeyType(value: RemoteKeyType) = value.type
}

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val entityId: Int,
    @TypeConverters(RemoteKeyType::class)
    val type: RemoteKeyType,
    val prevKey: Int?,
    val nextKey: Int?
)