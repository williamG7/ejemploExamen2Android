package com.example.apilist.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apilist.data.database.CharacterEntity
import com.example.apilist.viewmodel.APIviewmodel
import androidx.compose.runtime.setValue

@Composable
fun FavoritesScreen(
    navigateToDetail: (String) -> Unit,
    viewModel: APIviewmodel
) {
    // Estado observables
    val favorites by viewModel.favorites.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(true)
    var searchText by remember { mutableStateOf("") }

    // Cargar favoritos al iniciar
    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay personajes favoritos",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Barra de búsqueda
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar favoritos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Lista de favoritos
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(
                    favorites.filter {
                        it.name.contains(searchText, ignoreCase = true)
                    }
                ) { character ->
                    FavoriteCharacterItem(
                        character = character,
                        onClick = { navigateToDetail(character.name) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCharacterItem(
    character: CharacterEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}