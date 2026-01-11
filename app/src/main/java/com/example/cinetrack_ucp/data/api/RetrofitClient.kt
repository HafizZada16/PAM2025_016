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
}