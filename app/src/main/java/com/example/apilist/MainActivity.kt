package com.example.apilist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.apilist.data.network.SettingsRepository
import com.example.apilist.ui.navigation.Destinations
import com.example.apilist.ui.navigation.NavigationItem
import com.example.apilist.ui.navigation.NavigationWrapper
import com.example.apilist.ui.theme.APIListTheme
import com.example.apilist.viewmodel.APIviewmodel
import com.example.apilist.viewmodel.MyViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            APIListTheme {
                MyApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val myViewModel: APIviewmodel = viewModel(
        factory = MyViewModelFactory(SettingsRepository(LocalContext.current))
    )

    val isDarkTheme by myViewModel.isDarkTheme.observeAsState(false)

    APIListTheme(darkTheme = isDarkTheme) {
        var selectedItem: Int by remember { mutableIntStateOf(0) }

        val items = listOf(
            NavigationItem("Home", Icons.Default.Home, Destinations.Home, 0),
            NavigationItem("Favorites", Icons.Default.Favorite, Destinations.Favorites, 1)
        )

        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = item.index == selectedItem,
                            label = { Text(item.label) },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            onClick = {
                                selectedItem = index
                                navController.navigate(item.route)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavigationWrapper(
                navController = navController,
                viewModel = myViewModel
            )
        }
    }
}
