package com.example.cinetrack_ucp.data.api

import com.example.cinetrack_ucp.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {

    // Request GET ke endpoint "movie/popular"
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String, // Kunci API kita
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    // Tambahkan fungsi ini di dalam interface TmdbApiService
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String, // Kata kunci dari user
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

}