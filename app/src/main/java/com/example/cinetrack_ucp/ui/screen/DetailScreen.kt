package com.example.cinetrack_ucp.ui.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinetrack_ucp.model.Movie
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movie: Movie,
    viewModel: MovieViewModel,
    fromWatchlist: Boolean = false,
    onBack: () -> Unit
    ) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // State lokal untuk ikon favorit (REQ-10)
    var isFavorite by remember { mutableStateOf(false) }

    // Cek status favorit saat layar pertama kali dibuka
    LaunchedEffect(movie.id) {
        isFavorite = viewModel.isFavorite(movie.id)
    }

    // Di dalam DetailScreen
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi Pesanan") },
            text = {
                Column {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Lengkap") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    // Logika Validasi (Nama min 3 karakter & Email mengandung @)
                    if (name.length >= 3 && email.contains("@")) {
                        viewModel.bookTicket(movie, name, email)
                        showDialog = false
                        Toast.makeText(context, "Tiket berhasil dipesan!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Tampilkan pesan error jika tidak valid
                        val errorMessage = when {
                            name.length < 3 -> "Nama minimal harus 3 karakter!"
                            !email.contains("@") -> "Format email tidak valid (harus ada @)!"
                            else -> "Mohon isi semua data dengan benar!"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Pesan") }
            }
        )
    }

// Ubah tombol booking yang tadi:
    Button(onClick = { showDialog = true }) {
        Text("Booking Tiket Sekarang")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Film") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        // Simpan status sebelum diubah
                        val wasFavorite = isFavorite

                        viewModel.toggleFavorite(movie)
                        isFavorite = !isFavorite

                        // Tampilkan feedback visual (Toast)
                        val message = if (wasFavorite) "Dihapus dari Watchlist" else "Berhasil simpan ke Watchlist"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                        // LOGIKA BARU: Jika sebelumnya favorit (berarti sekarang menghapus),
                        // langsung panggil onBack() untuk balik ke halaman Watchlist
                        if (fromWatchlist && wasFavorite) {
                            onBack()
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Tampilan Poster (REQ-5) [cite: 410, 432]
            AsyncImage(
                model = movie.fullPosterUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Info Film (REQ-5) [cite: 410]
                Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Rilis: ${movie.releaseDate}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Rating: ⭐ ${movie.voteAverage}/10", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Sinopsis", style = MaterialTheme.typography.titleMedium)
                Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Trailer (REQ-6) [cite: 411, 440]
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=${movie.title}+trailer"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Watch Trailer")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { showDialog = true }) {
                    Text("Booking Tiket Sekarang")
                }
            }
        }

    }
}