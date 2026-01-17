package com.example.cinetrack_ucp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val title: String,
    val posterPath: String,
    val bookingTime: String, // Kita simpan waktu pesannya
    val customerName: String,
    val customerEmail: String
)