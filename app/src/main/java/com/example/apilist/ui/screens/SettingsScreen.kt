package com.example.apilist.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import com.example.apilist.viewmodel.APIviewmodel

@Composable
fun SettingsScreen(viewModel: APIviewmodel) {

    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val currentViewMode by viewModel.viewMode.observeAsState("List")
    var selectedViewMode by remember { mutableStateOf(currentViewMode) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Dark mode switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Modo oscuro",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 8.dp)
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { viewModel.onToggleTheme(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        // View mode selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Show Mode",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 8.dp)
            )

            MyDropdownMenu(
                selectedText = selectedViewMode,
                onValueChange = {
                    selectedViewMode = it
                    viewModel.setViewMode(it)
                },
                modifier = Modifier.width(120.dp)
            )
        }

        Spacer(modifier = Modifier.size(40.dp))

        Button(
            onClick = { viewModel.deleteAllFavorites() },
            colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Eliminar Favoritos",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun MyDropdownMenu(
    selectedText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("List", "Grid")

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.clickable { expanded = true },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(120.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}