package com.example.cinetrack_ucp.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

@Composable
fun DetailScreen(movie: Movie, viewModel: MovieViewModel, onBack: () -> Unit) {
    // State untuk memantau apakah film ini favorit atau bukan
    var isFav by remember { mutableStateOf(false) }

    // Fungsi helper untuk membuka YouTube
    fun launchTrailer(context: Context, movieTitle: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/results?search_query=$movieTitle+trailer")
        )
        context.startActivity(intent)
    }

    // Di dalam tombol "Watch Trailer" di DetailScreen:
    val context = LocalContext.current
    Button(onClick = { launchTrailer(context, movie.title) }) {
        Text("Watch Trailer")
    }
    // Cek status favorit saat layar dibuka
    LaunchedEffect(movie.id) {
        // isFav = viewModel.checkIsFavorite(movie.id)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.toggleFavorite(movie)
                isFav = !isFav
            }) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = movie.fullPosterUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(400.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rating: ${movie.voteAverage}/10", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)

                // Button Trailer sesuai REQ-6 [cite: 411]
                Button(
                    onClick = { /* Implement Intent YouTube */ },
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
                ) {
                    Text("Watch Trailer")
                }
            }
        }
    }
}