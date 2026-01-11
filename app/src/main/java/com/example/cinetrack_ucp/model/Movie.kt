package com.example.cinetrack_ucp.model

import com.google.gson.annotations.SerializedName

// 1. Ini wadah untuk RESPON UTAMA dari API
// Karena TMDB membungkus list film di dalam objek "results"
data class MovieResponse(
    @SerializedName("results")
    val results: List<Movie>
)

// 2. Ini wadah untuk SATU FILM
data class Movie(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?, // Bisa null kalau gak ada gambar

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("release_date")
    val releaseDate: String?
) {
    // Helper: URL lengkap untuk gambar (TMDB cuma kasih buntut file-nya doang)
    val fullPosterUrl: String
        get() = "https://image.tmdb.org/t/p/w500$posterPath"
}