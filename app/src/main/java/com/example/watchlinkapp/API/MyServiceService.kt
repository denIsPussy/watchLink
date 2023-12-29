package com.example.watchlinkapp.API

import com.example.watchlinkapp.API.Model.GenreRemote
import com.example.watchlinkapp.API.Model.MovieGenreCountRemote
import com.example.watchlinkapp.API.Model.MovieGenreCrossRefRemote
import com.example.watchlinkapp.API.Model.MovieRemote
import com.example.watchlinkapp.API.Model.ReleaseYearRemote
import com.example.watchlinkapp.API.Model.UserRemote
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyServerService {
    @POST("users")
    suspend fun createUser(
        @Body user: UserRemote,
    ): UserRemote

    @GET("users")
    suspend fun getUsers(): List<UserRemote>

    @GET("users")
    suspend fun getUser(
        @Query("userName") userName: String
    ): List<UserRemote>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: UserRemote,
    ): UserRemote

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int,
    ): UserRemote

    @GET("movies")
    suspend fun getMovies(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<MovieRemote>

    @GET("moviesByDate")
    suspend fun getMoviesByDate(
        @Query("start") startDate: String,
        @Query("end") endDate: String
    ): List<MovieRemote>

    @GET("moviesByGenreCount")
    suspend fun getMovieCountByGenre(): List<MovieGenreCountRemote>

    @GET("allReleaseYears")
    suspend fun getReleaseYears(): ReleaseYearRemote

    @GET("movies/{id}")
    suspend fun getMovie(
        @Path("id") id: Int,
    ): MovieRemote

    @POST("movies")
    suspend fun createMovie(
        @Body movie: MovieRemote,
    ): MovieRemote

    @PUT("movies/{id}")
    suspend fun updateMovie(
        @Path("id") id: Int,
        @Body movie: MovieRemote
    ): MovieRemote

    @DELETE("movies/{id}")
    suspend fun deleteMovie(
        @Path("id") id: Int,
    ): MovieRemote

    @GET("genres")
    suspend fun getGenres(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<GenreRemote>

    @GET("genres")
    suspend fun getAllGenres(): List<GenreRemote>

    @GET("genres/{id}")
    suspend fun getGenre(
        @Path("id") id: Int,
    ): GenreRemote

    @POST("genres")
    suspend fun createGenre(
        @Body genre: GenreRemote,
    ): GenreRemote

    @PUT("genres/{id}")
    suspend fun updateGenre(
        @Path("id") id: Int,
        @Body genre: GenreRemote,
    ): GenreRemote

    @DELETE("genres/{id}")
    suspend fun deleteGenre(
        @Path("id") id: Int,
    ): GenreRemote

    @POST("movieGenreCrossRefs")
    suspend fun createMovieGenre(
        @Body movieGenre: MovieGenreCrossRefRemote,
    )

    @GET("movieGenreCrossRefs")
    suspend fun getMoviesGenres(): List<MovieGenreCrossRefRemote>

    @DELETE("movieGenreCrossRefs/{id}")
    suspend fun deleteMovieGenre(
        @Path("id") id: Int,
    )

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8079/"

        @Volatile
        private var INSTANCE: MyServerService? = null

        fun getInstance(): MyServerService {
            return INSTANCE ?: synchronized(this) {
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC
                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(MyServerService::class.java)
                    .also { INSTANCE = it }
            }
        }
    }
}