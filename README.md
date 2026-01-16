# CineTrack - Movie Explorer App

Aplikasi Android untuk eksplorasi film menggunakan API TMDB, dilengkapi dengan fitur pencarian, watchlist lokal, dan sistem autentikasi.

## 🚀 Fitur Utama
* **List & Search Movies**: Menampilkan daftar film populer dan fitur pencarian menggunakan API TMDB.
* **Watchlist (Local DB)**: Menyimpan film favorit ke database lokal menggunakan Room Database sehingga bisa diakses secara offline.
* **Auth System**: Fitur Login dan Register yang terhubung ke backend Node.js.
* **Session Management**: Status login tersimpan secara otomatis menggunakan SharedPreferences.

## 🛠️ Tech Stack
* **Language**: Kotlin
* **UI**: Jetpack Compose
* **Architecture**: MVVM (Model-View-ViewModel)
* **Local DB**: Room Database
* **Networking**: Retrofit & GSON
* **Image Loading**: Coil

## ⚙️ Cara Menjalankan
1. Clone repository ini.
2. Pastikan Backend API sudah berjalan (lihat repository API).
3. Jika menggunakan emulator, pastikan `BASE_URL` di `RetrofitClient` mengarah ke `http://10.0.2.2:3000/`.
4. Build dan Run aplikasi.
