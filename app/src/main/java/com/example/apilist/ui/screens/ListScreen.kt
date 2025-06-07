package com.example.apilist.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.apilist.data.model.Data
import com.example.apilist.data.model.Info
import com.example.apilist.viewmodel.APIviewmodel
import com.example.apilist.data.model.Result
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
    val characters: Data by myViewModel.characters.observeAsState(
        Data(
            info = Info(0, "", 0, null),
            results = emptyList()
        )
    )
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
        val filteredList = characters.results.filter {
            it.name.contains(searchText)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            //TextField de búsqueda
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar Personaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            )

            //Lista o Grid
            if (opcionSeleccionada == "List") {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(filteredList) { it
                        CharacterItem(it, navigateToDetail)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(filteredList) { it
                        CharacterItem(it, navigateToDetail)
                    }
                }
            }
        }
    }
}


@Composable
fun CharacterItem(
    character: Result,
    navigateToDetail: (String) -> Unit,
    modoGrid: Boolean = false
) {
    Card(
        onClick = { navigateToDetail(character.url) }, // Esto sí permite click con Material3
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
                    model = character.image,
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
                    model = character.image,
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
