package com.example.cinetrack_ucp.data.repository

import com.example.cinetrack_ucp.data.BookingEntity
import com.example.cinetrack_ucp.data.api.TmdbApiService
import com.example.cinetrack_ucp.data.local.BookingDao
import com.example.cinetrack_ucp.data.local.MovieDao
import com.example.cinetrack_ucp.data.local.MovieEntity
import com.example.cinetrack_ucp.model.MovieResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MovieRepository(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao,
    private val bookingDao: BookingDao
) {
    // Fungsi API
    suspend fun getPopularMovies(apiKey: String): Response<MovieResponse> {
        return apiService.getPopularMovies(apiKey)
    }
    // Di dalam class MovieRepository
    suspend fun searchMovies(apiKey: String, query: String): Response<MovieResponse> {
        return apiService.searchMovies(apiKey, query)
    }

    // Fungsi Room (Local)
    fun getAllFavorites(): Flow<List<MovieEntity>> = movieDao.getAllFavorites()

    suspend fun insertFavorite(movie: MovieEntity) = movieDao.insertFavorite(movie)

    suspend fun deleteFavorite(movie: MovieEntity) = movieDao.deleteFavorite(movie)

    suspend fun isFavorite(id: Int): Boolean = movieDao.isFavorite(id)

    // Fungsi ini akan menggunakan fungsi di BookingDao
    fun getAllBookings() = bookingDao.getAllBookings()

    suspend fun insertBooking(booking: BookingEntity) {
        bookingDao.insertBooking(booking)
    }

    suspend fun deleteBooking(booking: BookingEntity) {
        bookingDao.deleteBooking(booking)
    }
}