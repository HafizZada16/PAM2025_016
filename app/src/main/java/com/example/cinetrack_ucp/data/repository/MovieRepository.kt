package com.example.cinetrack_ucp.data.repository

import com.example.cinetrack_ucp.data.api.TmdbApiService
import com.example.cinetrack_ucp.data.local.MovieDao
import com.example.cinetrack_ucp.data.local.MovieEntity
import com.example.cinetrack_ucp.model.MovieResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MovieRepository(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao
) {
    // Fungsi API
    suspend fun getPopularMovies(apiKey: String): Response<MovieResponse> {
        return apiService.getPopularMovies(apiKey)
    }

    // Fungsi Room (Local)
    fun getAllFavorites(): Flow<List<MovieEntity>> = movieDao.getAllFavorites()

    suspend fun insertFavorite(movie: MovieEntity) = movieDao.insertFavorite(movie)

    suspend fun deleteFavorite(movie: MovieEntity) = movieDao.deleteFavorite(movie)

    suspend fun isFavorite(id: Int): Boolean = movieDao.isFavorite(id)
}