package com.example.cinetrack_ucp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cinetrack_ucp.BuildConfig
import com.example.cinetrack_ucp.CineTrackApp
import com.example.cinetrack_ucp.data.local.MovieEntity
import com.example.cinetrack_ucp.data.repository.MovieRepository
import com.example.cinetrack_ucp.model.Movie
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.cinetrack_ucp.data.api.RetrofitClient
import com.example.cinetrack_ucp.model.AuthRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MovieViewModel(application: Application, private val repository: MovieRepository) : AndroidViewModel(application) {

    private val sharedPref = application.getSharedPreferences("CineTrackPrefs", Context.MODE_PRIVATE)
    // State (Kondisi UI saat ini)
    // Awal buka aplikasi, statusnya "Loading"
    var movieUiState: MovieUIState by mutableStateOf(MovieUIState.Loading)
        private set

    init {
        println("DEBUG_API_KEY: ${BuildConfig.API_KEY}")
        getPopularMovies()
    }

    // Di dalam class MovieViewModel
    suspend fun isFavorite(movieId: Int): Boolean {
        return repository.isFavorite(movieId)
    }

    // --- FUNGSI LOGIN ---
    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.login(AuthRequest(username, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    // Simpan status login jika berhasil
                    saveSession(username)
                    onResult(true, "Selamat datang, $username!")
                } else {
                    onResult(false, response.body()?.message ?: "Login Gagal")
                }
            } catch (e: Exception) {
                onResult(false, "Tidak dapat terhubung ke server. Pastikan backend jalan.")
            }
        }
    }
    // --- FUNGSI REGISTER ---
    fun register(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.register(AuthRequest(username, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    onResult(true, "Akun berhasil dibuat, silakan login.")
                } else {
                    onResult(false, response.body()?.message ?: "Registrasi Gagal")
                }
            } catch (e: Exception) {
                onResult(false, "Kesalahan jaringan.")
            }
        }
    }

    // --- SESSION HELPERS ---
    private fun saveSession(username: String) {
        sharedPref.edit().apply {
            putString("USERNAME", username)
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = sharedPref.getBoolean("IS_LOGGED_IN", false)

    fun logout(onNavigate: () -> Unit) {
        sharedPref.edit().clear().apply()
        onNavigate()
    }

    // Fungsi untuk mengambil semua data favorit (Watchlist)
// Mengonversi MovieEntity kembali ke Movie model untuk ditampilkan di UI
    val favoriteMovies: StateFlow<List<Movie>> = repository.getAllFavorites()
        .map { entities ->
            entities.map { entity ->
                Movie(
                    id = entity.movie_id,
                    title = entity.title,
                    overview = entity.overview,
                    posterPath = entity.poster_path,
                    voteAverage = entity.rating,
                    releaseDate = entity.release_date
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    // Tambahkan fungsi ini di dalam class MovieViewModel
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val entity = MovieEntity(
                movie_id = movie.id,
                title = movie.title,
                poster_path = movie.posterPath,
                overview = movie.overview,
                rating = movie.voteAverage,
                release_date = movie.releaseDate,
                genre = "" // Bisa ditambahkan jika data genre tersedia
            )

            if (repository.isFavorite(movie.id)) {
                repository.deleteFavorite(entity)
                // Tampilkan Toast "Dihapus" sesuai flowchart [cite: 459]
            } else {
                repository.insertFavorite(entity)
                // Tampilkan Toast "Disimpan" sesuai flowchart [cite: 459]
            }
        }
    }

    // State untuk menyimpan teks pencarian di UI
    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery // Update teks di UI
        if (newQuery.isEmpty()) {
            getPopularMovies() // Jika kosong, balikkan ke daftar populer
        } else {
            searchMovies(newQuery) // Jika ada teks, cari ke API TMDB
        }
    }

    fun searchMovies(query: String) {
        if (query.isEmpty()) {
            getPopularMovies() // Jika kosong, balikkan ke daftar populer
            return
        }

        viewModelScope.launch {
            movieUiState = MovieUIState.Loading
            try {
                val response = repository.searchMovies(com.example.cinetrack_ucp.BuildConfig.API_KEY, query)
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    movieUiState = MovieUIState.Success(movies)
                } else {
                    movieUiState = MovieUIState.Error("Pencarian gagal")
                }
            } catch (e: Exception) {
                movieUiState = MovieUIState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // 1. Ambil instance Application (CineTrackApp)
                val application = (this[APPLICATION_KEY] as CineTrackApp)

                // 2. Kirim DUA parameter: application DAN repository
                MovieViewModel(
                    application = application,
                    repository = application.repository
                )
            }
        }
    }


}