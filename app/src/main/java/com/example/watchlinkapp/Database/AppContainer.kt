package com.example.watchlinkapp.Database

import android.content.Context
import com.example.watchlinkapp.API.MyServerService
import com.example.watchlinkapp.API.Repository.RestGenreRepository
import com.example.watchlinkapp.API.Repository.RestMovieGenreRepository
import com.example.watchlinkapp.API.Repository.RestMovieRepository
import com.example.watchlinkapp.API.Repository.RestUserRepository
import com.example.watchlinkapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.watchlinkapp.Entities.Repository.Genre.OfflineGenreRepository
import com.example.watchlinkapp.Entities.Repository.Movie.OfflineMovieRepository
import com.example.watchlinkapp.Entities.Repository.MovieGenre.OfflineMovieGenreRepository
import com.example.watchlinkapp.Entities.Repository.User.OfflineUserRepository

interface AppContainer {
    val movieRestRepository: RestMovieRepository
    val genreRestRepository: RestGenreRepository
    val userRestRepository: RestUserRepository
    val movieRestGenreRepository: RestMovieGenreRepository

    companion object {
        const val LIMIT_MOVIES = 5
        const val LIMIT_GENRES = 5
    }
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val movieRepository: OfflineMovieRepository by lazy {
        OfflineMovieRepository(AppDatabase.getInstance(context).movieDao())
    }
    private val genreRepository: OfflineGenreRepository by lazy {
        OfflineGenreRepository(AppDatabase.getInstance(context).genreDao())
    }
    private val userRepository: OfflineUserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }
    private val movieGenreRepository: OfflineMovieGenreRepository by lazy {
        OfflineMovieGenreRepository(AppDatabase.getInstance(context).movieWithGenresDao())
    }
    private val remoteKeyRepository: OfflineRemoteKeyRepository by lazy {
        OfflineRemoteKeyRepository(AppDatabase.getInstance(context).remoteKeysDAO())
    }
    override val movieRestRepository: RestMovieRepository by lazy {
        RestMovieRepository(
            MyServerService.getInstance(),
            movieRepository,
            remoteKeyRepository,
            movieRestGenreRepository,
            movieGenreRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val genreRestRepository: RestGenreRepository by lazy {
        RestGenreRepository(
            MyServerService.getInstance(),
            genreRepository,
            remoteKeyRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val userRestRepository: RestUserRepository by lazy {
        RestUserRepository(
            MyServerService.getInstance(),
            userRepository
        )
    }
    override val movieRestGenreRepository: RestMovieGenreRepository by lazy {
        RestMovieGenreRepository(
            MyServerService.getInstance(),
            movieGenreRepository
        )
    }
}