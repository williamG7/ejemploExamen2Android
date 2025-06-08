package com.example.apilist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.apilist.viewmodel.APIviewmodel

@Composable
fun DetailScreen(navigateBack: () -> Unit, characterName: String ) {
    val viewModel: APIviewmodel = viewModel()
    val character by viewModel.characterDetail.observeAsState()
    val loading by viewModel.loading.observeAsState(true)
    val isFavorite by viewModel.isFavorite.observeAsState(false)
    val context = LocalContext.current

    // Cargar detalles del personaje al iniciar
    LaunchedEffect(characterName) {
        viewModel.getCharacterByName(characterName)
    }

    if (loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
    } else if (character == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Personaje no encontrado")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = navigateBack) {
                Text("Volver")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón de favorito
            IconButton(
                onClick = { viewModel.toggleFavorite() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Imagen del personaje
            Image(
                painter = rememberAsyncImagePainter(character?.imageUrl),
                contentDescription = "Imagen de ${character?.name}",
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                text = character?.name ?: "",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Detalles
            character?.let {
                DetailItem("Especie", it.species ?: "Desconocida")
                DetailItem("Planeta natal", it.homeworld ?: "Desconocido")
                DetailItem("Descripción", it.description)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = navigateBack) {
                Text("Volver a la lista")
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
