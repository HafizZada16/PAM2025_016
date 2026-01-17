package com.example.cinetrack_ucp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CineTrackTopBar(
    title: String,
    viewModel: MovieViewModel,
    navController: NavHostController,
    showWatchlistButton: Boolean = true,
    onWatchlistClick: () -> Unit = {},
    onLogoutSuccess: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            // Tombol Ke Watchlist (Hanya muncul di Home)
            if (showWatchlistButton) {
                IconButton(onClick = onWatchlistClick) {
                    Icon(Icons.Default.Favorite, contentDescription = "Watchlist")
                }
            }

            IconButton(onClick = { navController.navigate("booking") }) {
                Icon(Icons.Default.ConfirmationNumber, null)
            }

            // Tombol Logout
            IconButton(onClick = {
                viewModel.logout {
                    onLogoutSuccess()
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            }
        }
    )
}