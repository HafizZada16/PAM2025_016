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
    // NavController adalah pengontrol navigasi utama
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home" // Halaman pertama yang dibuka
    ) {
        // 1. Route Halaman Utama (Daftar Film)
        composable("home") {
            MovieScreen(
                viewModel = viewModel,
                onMovieClick = { movie ->
                    navController.navigate("detail/${movie.id}")
                }
            )
        }

        // 2. Route Halaman Detail (Menerima parameter movieId)
        composable(
            route = "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")

            // Mencari data film yang diklik dari state ViewModel
            val movie = (viewModel.movieUiState as? MovieUIState.Success)
                ?.movies?.find { it.id == movieId }

            if (movie != null) {
                DetailScreen(
                    movie = movie,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() } // Fungsi tombol kembali
                )
            }
        }

        composable("watchlist") {
            WatchlistScreen(
                viewModel = viewModel,
                onMovieClick = { id -> navController.navigate("detail/$id") },
                onBack = { navController.popBackStack() }
            )
        }
    }
}