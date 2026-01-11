package com.example.cinetrack_ucp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinetrack_ucp.ui.screen.DetailScreen
import com.example.cinetrack_ucp.ui.screen.MovieScreen
import com.example.cinetrack_ucp.ui.screen.WatchlistScreen
import com.example.cinetrack_ucp.ui.viewmodel.MovieUIState
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel

@Composable
fun CineTrackNavGraph(viewModel: MovieViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 1. Halaman Home
        composable("home") {
            MovieScreen(
                viewModel = viewModel,
                onMovieClick = { movie ->
                    // Pastikan movie.id adalah Int
                    navController.navigate("detail/${movie.id}")
                },
                onWatchlistClick = {
                    navController.navigate("watchlist")
                }
            )
        }

        // 2. Halaman Detail (Menerima Argumen ID)
        composable(
            route = "detail/{movieId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Mengambil ID dari argumen navigasi
            val movieId = backStackEntry.arguments?.getInt("movieId")

            // Mencari data film dari State Success di ViewModel
            val movie = (viewModel.movieUiState as? MovieUIState.Success)
                ?.movies?.find { it.id == movieId }

            movie?.let {
                DetailScreen(
                    movie = it,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // 3. Halaman Watchlist
        composable("watchlist") {
            WatchlistScreen(
                viewModel = viewModel,
                onMovieClick = { id ->
                    navController.navigate("detail/$id")
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}