package com.example.cinetrack_ucp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Int) -> Unit, // Navigasi ke detail (REQ-4)
    onBack: () -> Unit
) {
    // Observasi data favorit dari Room (REQ-11, REQ-12)
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Watchlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (favoriteMovies.isEmpty()) {
            // Tampilan jika watchlist kosong (REQ-12)
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada film di watchlist")
            }
        } else {
            // Menampilkan list favorit (REQ-11)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteMovies) { movie ->
                    MovieItem(
                        movie = movie,
                        onClick = { onMovieClick(movie.id) }
                    )
                }
            }
        }
    }
}