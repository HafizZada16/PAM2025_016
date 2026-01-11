package com.example.cinetrack_ucp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinetrack_ucp.BuildConfig
import com.example.cinetrack_ucp.data.repository.MovieRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel : ViewModel() {

    // Repository (Sumber Data)
    private val repository = MovieRepository()

    // State (Kondisi UI saat ini)
    // Awal buka aplikasi, statusnya "Loading"
    var movieUiState: MovieUIState by mutableStateOf(MovieUIState.Loading)
        private set

    init {
        // Begitu ViewModel dibuat, langsung ambil data
        getPopularMovies()
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            // 1. Set status ke Loading dulu (biar muncul progress bar)
            movieUiState = MovieUIState.Loading

            try {
                // 2. Panggil API pakai Key dari BuildConfig
                // Pastikan project sudah di-build supaya BuildConfig kedetect
                val response = repository.getPopularMovies(BuildConfig.API_KEY)

                if (response.isSuccessful) {
                    // 3. Jika Sukses, ambil list filmnya
                    // Tanda tanya (?) dan elvis (?:) untuk jaga-jaga kalau null
                    val movies = response.body()?.results ?: emptyList()
                    movieUiState = MovieUIState.Success(movies)
                } else {
                    // 4. Jika response code bukan 200 (misal 401/404)
                    movieUiState = MovieUIState.Error("Gagal memuat data: ${response.code()}")
                }

            } catch (e: IOException) {
                // 5. Jika tidak ada internet
                movieUiState = MovieUIState.Error("Tidak ada koneksi internet")
            } catch (e: Exception) {
                // 6. Error lain-lain
                movieUiState = MovieUIState.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}