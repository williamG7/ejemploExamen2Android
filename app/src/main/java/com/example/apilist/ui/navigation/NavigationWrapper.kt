package com.example.apilist.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.apilist.ui.navigation.Destinations.Favorites
import com.example.apilist.ui.navigation.Destinations.Home
import com.example.apilist.ui.navigation.Destinations.Pantalla11
import com.example.apilist.ui.navigation.Destinations.Settings
import com.example.apilist.ui.screens.DetalleScreen
import com.example.apilist.ui.screens.FavoritesScreen
import com.example.apilist.ui.screens.ListScreen
import com.example.apilist.ui.screens.SettingsScreen
import com.example.apilist.viewmodel.APIviewmodel


@Composable
fun NavigationWrapper(
    navController: NavHostController, viewModel: APIviewmodel,
) {
    NavHost(navController, Home) {
        composable<Home> {
            ListScreen { characterId ->
                navController.navigate(Pantalla11(characterId))
            }
        }
        composable<Favorites> {
            FavoritesScreen(
                navigateToDetail = { characterId ->
                    navController.navigate(
                        Pantalla11(
                            characterId
                        )
                    )
                },
                myViewModel = viewModel
            )
        }
        composable<Settings> {
            SettingsScreen(
                viewModel = viewModel
            )
        }
        composable<Pantalla11> { backStackEntry ->
            val pantallaDetail = backStackEntry.toRoute<Pantalla11>()
            DetalleScreen(pantallaDetail.myParameter) {
                navController.popBackStack()
            }
        }
    }
}