package com.example.cinetrack_ucp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // Import ini penting
import com.example.cinetrack_ucp.ui.screen.MovieScreen
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel
import com.example.cinetrack_ucp.ui.theme.Cinetrack_ucpTheme

class MainActivity : ComponentActivity() {

    // CARA BENAR: Inisialisasi instance ViewModel
    private val mViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cinetrack_ucpTheme {
                // Panggil dengan instance 'mViewModel', bukan Class 'MovieViewModel'
                MovieScreen(viewModel = mViewModel)
            }
        }
    }
}