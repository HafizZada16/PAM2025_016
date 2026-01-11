package com.example.cinetrack_ucp

import android.app.Application
import com.example.cinetrack_ucp.data.api.RetrofitClient
import com.example.cinetrack_ucp.data.local.AppDatabase
import com.example.cinetrack_ucp.data.repository.MovieRepository

class CineTrackApp : Application() {
    // Inisialisasi Repository secara global agar bisa dipakai ViewModel
    val repository: MovieRepository by lazy {
        val database = AppDatabase.getDatabase(this)
        MovieRepository(RetrofitClient.instance, database.movieDao())
    }
}