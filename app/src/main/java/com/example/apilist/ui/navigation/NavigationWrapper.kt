package com.example.apilist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.apilist.ui.screens.DetailScreen
import com.example.apilist.ui.screens.FavoritesScreen
import com.example.apilist.ui.screens.ListScreen
import com.example.apilist.viewmodel.APIviewmodel

@Composable
fun NavigationWrapper(
    navController: NavHostController,
    viewModel: APIviewmodel
) {
    NavHost(navController, startDestination = Destinations.Home) {
        // Pantalla de Listado
        composable<Destinations.Home> {
            ListScreen(
                navigateToDetail = { characterUrl ->
                    navController.navigate(Destinations.DetailScreen(characterUrl))
                }
            )
        }

        // Pantalla de Favoritos
        composable<Destinations.Favorites> {
            FavoritesScreen(
                navigateToDetail = { characterUrl ->
                    navController.navigate(Destinations.DetailScreen(characterUrl))
                },
                viewModel = viewModel
            )
        }

        // Pantalla de Detalle
        composable<Destinations.DetailScreen> { backStackEntry ->
            val detail = backStackEntry.toRoute<Destinations.DetailScreen>()
            DetailScreen(
                characterName = detail.characterUrl,
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}