package com.example.cinetrack_ucp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinetrack_ucp.model.Movie
import com.example.cinetrack_ucp.ui.viewmodel.MovieUIState
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Movie) -> Unit,
    onWatchlistClick: () -> Unit
) {
    val state = viewModel.movieUiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CineTrack", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    IconButton(onClick = onWatchlistClick) {
                        Icon(Icons.Default.Favorite, contentDescription = "Watchlist", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { paddingValues ->
        // Gunakan Column untuk memisahkan Search Bar dan List Film
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Memberi jarak agar tidak tertutup TopAppBar
        ) {
            // 1. Search Bar (REQ-2)
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = {
                    viewModel.onSearchQueryChange(it)
                    viewModel.searchMovies(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari judul film...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Beri sedikit jarak antara Search Bar dan Grid
            Spacer(modifier = Modifier.height(8.dp))

            // 2. Area Konten (Grid Film)
            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is MovieUIState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is MovieUIState.Success -> {
                        MovieGrid(movies = state.movies, onMovieClick = onMovieClick)
                    }
                    is MovieUIState.Error -> {
                        Text(
                            text = state.message,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieGrid(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 Kolom sesuai mockup SRS [cite: 462]
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                onClick = { onMovieClick(movie) } // Mengirim data film saat diklik
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick // Membuat seluruh area card bisa diklik
    ) {
        Column {
            AsyncImage(
                model = movie.fullPosterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp),
                maxLines = 1
            )
        }
    }
}