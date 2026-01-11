package com.example.cinetrack_ucp.ui.viewmodel

import com.example.cinetrack_ucp.model.Movie

// Ini mendefinisikan 3 kondisi layar kita
sealed interface MovieUIState {
    data object Loading : MovieUIState                  // Lagi muter loading
    data class Success(val movies: List<Movie>) : MovieUIState // Berhasil dapet data
    data class Error(val message: String) : MovieUIState       // Gagal (No internet/API error)
}