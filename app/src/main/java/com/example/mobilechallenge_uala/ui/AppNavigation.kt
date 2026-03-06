package com.example.mobilechallenge_uala.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation(viewModel: CityViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "main") {

        // 1. Ruta Principal
        composable("main") {
            MainScreen(
                uiState = uiState,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                onToggleFavoriteFilter = { viewModel.onToggleFavoriteFilter() },
                onCityClick = { city ->
                    //navegar ql mapa usando el id de la ciudad
                    navController.navigate("map/${city.id}")
                },
                onInfoClick = { city ->
                    navController.navigate("info/${city.id}")
                },
                onFavoriteToggle = { viewModel.toggleCityFavorite(it) }
            )
        }

        // 2. Ruta del Mapa
        composable(
            route = "map/{cityId}",
            arguments = listOf(navArgument("cityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId") ?: 0
            val city = uiState.allCities.find { it.id == cityId }

            if (city != null) {
                MapScreen(
                    city = city,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // 3. Ruta de Información
        composable(
            route = "info/{cityId}",
            arguments = listOf(navArgument("cityId") { type = NavType.IntType })
        ) { backStackEntry ->
            val cityId = backStackEntry.arguments?.getInt("cityId") ?: 0
            val city = uiState.allCities.find { it.id == cityId }

            if (city != null) {
                InfoScreen(
                    city = city,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}