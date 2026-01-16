package com.example.cinetrack_ucp.model

import com.google.gson.annotations.SerializedName

// Untuk mengirim data ke Node.js
data class AuthRequest(
    val username: String,
    val password: String
)

// Untuk menerima jawaban dari Node.js
data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Int? = null
)