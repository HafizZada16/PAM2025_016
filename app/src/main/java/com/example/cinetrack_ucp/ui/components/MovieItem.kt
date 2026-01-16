package com.example.cinetrack_ucp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cinetrack_ucp.model.Movie

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp), // Corner lebih melengkung
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(220.dp)) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Rating badge di pojok kanan atas
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(bottomStart = 12.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "⭐ ${String.format("%.1f", movie.voteAverage)}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}