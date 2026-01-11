package com.example.cinetrack_ucp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun MovieScreen(viewModel: MovieViewModel) {
    // Mengamati perubahan state dari ViewModel
    val state = viewModel.movieUiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("CineTrack") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (state) {
                is MovieUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MovieUIState.Success -> {
                    MovieGrid(movies = state.movies)
                }
                is MovieUIState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieGrid(movies: List<Movie>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 Kolom seperti di mockup [cite: 462]
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp)
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