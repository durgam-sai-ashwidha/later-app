package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.AppDatabase
import com.example.data.MemoryRepository
import com.example.ui.screens.CaptureScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MemoryDetailScreen
import com.example.ui.screens.PaywallScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.LaterTheme
import com.example.viewmodel.MemoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = MemoryRepository(database.memoryDao())

        setContent {
            LaterTheme {
                val navController = rememberNavController()
                val memoryViewModel: MemoryViewModel = viewModel { MemoryViewModel(repository) }

                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = memoryViewModel,
                                onNavigateToCapture = { navController.navigate("capture") },
                                onNavigateToSearch = { navController.navigate("search") },
                                onNavigateToDetail = { id -> navController.navigate("detail/$id") },
                                onNavigateToPaywall = { navController.navigate("paywall") }
                            )
                        }

                        composable("capture") {
                            CaptureScreen(
                                viewModel = memoryViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onMemorySaved = { navController.popBackStack() }
                            )
                        }

                        composable("search") {
                            SearchScreen(
                                viewModel = memoryViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToDetail = { id -> navController.navigate("detail/$id") }
                            )
                        }

                        composable(
                            route = "detail/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val memoryId = backStackEntry.arguments?.getString("id") ?: ""
                            MemoryDetailScreen(
                                memoryId = memoryId,
                                viewModel = memoryViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("paywall") {
                            PaywallScreen(
                                viewModel = memoryViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
