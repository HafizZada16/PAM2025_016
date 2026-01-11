package com.example.cinetrack_ucp.data.repository

import com.example.cinetrack_ucp.data.api.RetrofitClient
import com.example.cinetrack_ucp.model.MovieResponse
import retrofit2.Response

class MovieRepository {
    // Fungsi untuk memanggil API
    suspend fun getPopularMovies(apiKey: String): Response<MovieResponse> {
        return RetrofitClient.instance.getPopularMovies(apiKey)
    }
}