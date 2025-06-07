package com.example.apilist.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.apilist.data.network.SettingsRepository
import com.example.apilist.viewmodel.APIviewmodel
import com.example.apilist.viewmodel.MyViewModelFactory

@Composable
fun DetalleScreen(characterUrl: String, navigateBack: () -> Unit) {
    val myViewModel: APIviewmodel = viewModel(
        factory = MyViewModelFactory(SettingsRepository(LocalContext.current))
    )

    val actualCharacter by myViewModel.actualCharacter.observeAsState()
    val showLoading: Boolean by myViewModel.loading.observeAsState(true)
    val isFavorite: Boolean by myViewModel.isFavorite.observeAsState(false)
    val showToast: Boolean by myViewModel.showToast.observeAsState(false)
    val context = LocalContext.current

    if (showLoading) {
        myViewModel.getCharacter(characterUrl)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center the loading indicator
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }
    } else {
        actualCharacter?.let { character ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Center the content
            ) {
                // Botón de favorito
                IconButton(onClick = { myViewModel.saveFavorite() }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen del personaje
                Image(
                    painter = rememberAsyncImagePainter(character.image),
                    contentDescription = "Imagen de ${character.name}",
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Nombre
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                //Estado
                DetalleText(letra = "Estado", valor = character.status)

                // Especie
                DetalleText(letra = "Especie", valor = character.species)

                //Vivo o muerto
                val sigueVivo = if (character.status.lowercase() == "alive") "Sí" else "No"
                DetalleText(letra = "¿Sigue vivo?", valor = sigueVivo)

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = navigateBack) {
                    Text("Volver", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

    if (showToast) {
        Toast.makeText(context, myViewModel.getToastMessage(), Toast.LENGTH_SHORT).show()
    }
}
//Funcion de detalles de textos
@Composable
fun DetalleText(letra: String, valor: String) {
    Text(
        text = "$letra: $valor",
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 20.sp
        ),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
