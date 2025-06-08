package com.example.apilist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.apilist.data.model.Character
import com.example.apilist.viewmodel.APIviewmodel
import com.example.apilist.data.network.SettingsRepository
import com.example.apilist.viewmodel.MyViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navigateToDetail: (String) -> Unit) {
    val myViewModel: APIviewmodel = viewModel(
        factory = MyViewModelFactory(SettingsRepository(LocalContext.current))
    )

    var searchText by remember { mutableStateOf("") }
    val opcionSeleccionada by myViewModel.viewMode.observeAsState()
    val characters: List<Character> by myViewModel.characters.observeAsState(emptyList())
    val showLoading: Boolean by myViewModel.loading.observeAsState(true)

    if (showLoading) {
        myViewModel.getCharacters()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else {
        val filteredList = characters.filter {
            it.name.contains(searchText, ignoreCase = true)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // TextField de búsqueda
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar Personaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            )

            // Lista o Grid
            if (opcionSeleccionada == "List") {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(filteredList) { character ->
                        CharacterItem(character, navigateToDetail)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(filteredList) { character ->
                        CharacterItem(character, navigateToDetail, modoGrid = true)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    navigateToDetail: (String) -> Unit,
    modoGrid: Boolean = false
) {
    Card(
        onClick = { navigateToDetail(character.name) }, // usamos name para el detalle
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
    ) {
        if (modoGrid) {
            // MODO GRID
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = character.imageUrl,
                    contentDescription = "Imagen de ${character.name}",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            // MODO LISTA
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = character.imageUrl,
                    contentDescription = "Imagen de ${character.name}",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
