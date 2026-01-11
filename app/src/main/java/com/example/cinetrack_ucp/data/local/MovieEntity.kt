package com.example.cinetrack_ucp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies") // Sesuai rancangan FAT di SRS
data class MovieEntity(
    @PrimaryKey
    val movie_id: Int, // ID unik dari TMDB
    val title: String,
    val poster_path: String?,
    val overview: String,
    val rating: Double,
    val release_date: String?,
    val genre: String?
)