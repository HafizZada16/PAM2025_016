package com.example.cinetrack_ucp

import android.app.Application
import com.example.cinetrack_ucp.data.api.RetrofitClient
import com.example.cinetrack_ucp.data.api.TmdbApiService
import com.example.cinetrack_ucp.data.local.AppDatabase
import com.example.cinetrack_ucp.data.local.MovieDao
import com.example.cinetrack_ucp.data.repository.MovieRepository

class CineTrackApp : Application() {
    // Definisi database dan repository secara langsung
    lateinit var database: AppDatabase
    lateinit var repository: MovieRepository

    override fun onCreate() {
        super.onCreate()
        // Inisialisasi dilakukan saat aplikasi pertama kali dijalankan
        database = AppDatabase.getDatabase(this)
        repository = MovieRepository(
            RetrofitClient.instance, // Argumen ke-1: TmdbApiService
            database.movieDao(),    // Argumen ke-2: MovieDao
            database.bookingDao()
        )
    }
}