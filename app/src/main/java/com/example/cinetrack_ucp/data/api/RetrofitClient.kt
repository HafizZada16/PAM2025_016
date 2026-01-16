package com.example.cinetrack_ucp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Alamat utama server TMDB
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // Membuat instance Retrofit (Cuma dibuat sekali seumur hidup aplikasi)
    val instance: TmdbApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(TmdbApiService::class.java)
    }

    // Di dalam object RetrofitClient, tambahkan ini:
    private const val BASE_URL_BACKEND = "http://10.0.2.2:3000/" // IP khusus emulator ke localhost

    val authService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_BACKEND)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}