package com.example.cinetrack_ucp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinetrack_ucp.ui.screen.DetailScreen
import com.example.cinetrack_ucp.ui.screen.LoginScreen
import com.example.cinetrack_ucp.ui.screen.MovieScreen
import com.example.cinetrack_ucp.ui.screen.RegisterScreen
import com.example.cinetrack_ucp.ui.screen.WatchlistScreen
import com.example.cinetrack_ucp.ui.viewmodel.MovieUIState
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel
import androidx.compose.runtime.collectAsState


@Composable
fun CineTrackNavGraph(viewModel: MovieViewModel) {
    val navController = rememberNavController()

    val startDest = if (viewModel.isLoggedIn()) "home" else "login"
    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true } // Hapus login dari backstack
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { navController.popBackStack() }, // Balik ke login
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable("home") {
            MovieScreen(
                viewModel = viewModel,
                navController = navController,
                onMovieClick = { movie -> navController.navigate("detail/${movie.id}") },
                onWatchlistClick = { navController.navigate("watchlist") }
            )
        }

        // 2. Halaman Detail (Menerima Argumen ID)
        composable(
            route = "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0

            // 1. Cari di daftar film Home (API)
            val movieFromHome = (viewModel.movieUiState as? MovieUIState.Success)
                ?.movies?.find { it.id == movieId }

            // 2. Cari di daftar film Watchlist (LOKAL)
            // TAMBAHKAN .value SETELAH favoriteMovies
            val movieFromWatchlist = viewModel.favoriteMovies.collectAsState().value.find { it.id == movieId }

            // 3. Gabungkan hasilnya (kalau di home gak ada, cari di watchlist)
            val movie = movieFromHome ?: movieFromWatchlist

            movie?.let {
                DetailScreen(
                    movie = it,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            } ?: run {
                // Ini muncul kalau film tidak ketemu sama sekali
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Film tidak ditemukan")
                }
            }
        }

        // 3. Halaman Watchlist
        composable("watchlist") {
            WatchlistScreen(
                viewModel = viewModel,
                onMovieClick = { selectedMovie -> // Beri nama parameter yang jelas
                    navController.navigate("detail/${selectedMovie.id}?fromWatchlist=true")
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}