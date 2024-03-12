package com.example.clickerdestroyer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clickerdestroyer.ui.theme.ClickerDestroyerTheme
import com.example.clickerdestroyer.view.MainContent
import com.example.clickerdestroyer.view.Shop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        viewModel.getData("Jacko")
        setContent {
            ClickerDestroyerTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "MainContent"
                    ) {
                        composable("MainContent") {
                            MainContent(navController = navController, viewModel = viewModel)
                        }
                        composable("Shop") {
                            Shop(navController, viewModel)
                        }
                    }
                }
            }
        }
    }

}

