package com.example.cinetrack_ucp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // Import ini penting
import com.example.cinetrack_ucp.ui.CineTrackNavGraph
import com.example.cinetrack_ucp.ui.viewmodel.MovieViewModel
import com.example.cinetrack_ucp.ui.theme.Cinetrack_ucpTheme

class MainActivity : ComponentActivity() {

    // Tambahkan parameter factory di sini
    private val mViewModel: MovieViewModel by viewModels { MovieViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cinetrack_ucpTheme {
                CineTrackNavGraph(viewModel = mViewModel)
            }
        }
    }
}