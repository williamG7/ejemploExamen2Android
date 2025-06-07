package com.example.apilist.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.apilist.data.database.CharacterEntity
import com.example.apilist.viewmodel.APIviewmodel
import androidx.compose.runtime.setValue


@Composable
fun FavoritesScreen(
    navigateToDetail: (String) -> Unit,
    myViewModel: APIviewmodel
) {
    val characters by myViewModel.favorites.observeAsState(emptyList())
    val showLoading: Boolean by myViewModel.loading.observeAsState(true)

    var searchText by remember { mutableStateOf("") }


    //Actualiza la lista de favoritos al entrar en la pantalla
    LaunchedEffect(Unit) {
        myViewModel.getFavorites()
    }

    if (showLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }
    } else {
        //Si no hay favoritos, muestra un mensaje
        if (characters.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay favoritos",
                    textAlign = TextAlign.Center
                )
            }
            //Si hay favoritos, muestra la lista
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                //TextField para buscar personajes
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Buscar Personaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 15.dp)
                )

                // Lista de personajes favoritos filtrados
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp)
                ) {
                    items(characters.filter { it.name.contains(searchText) }) { character ->
                        CharacterItem(character) {
                            navigateToDetail(character.url)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: CharacterEntity, onClick: () -> Unit) {
    Card(
        border = BorderStroke(2.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}