package com.example.cinetrack_ucp.data.api

import com.example.cinetrack_ucp.model.AuthRequest
import com.example.cinetrack_ucp.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>
}