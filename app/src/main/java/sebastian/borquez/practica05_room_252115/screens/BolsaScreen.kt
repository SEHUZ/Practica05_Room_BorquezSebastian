package sebastian.borquez.practica05_room_252115.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import sebastian.borquez.practica05_room_252115.data.PokemonEntity
import sebastian.borquez.practica05_room_252115.viewModel.PokemonViewModel

@Composable
fun BolsaScreen(pokemonViewModel: PokemonViewModel){

    val searchQuery by pokemonViewModel.searchQuery.collectAsStateWithLifecycle()
    val typeFilter by pokemonViewModel.typeFilter.collectAsStateWithLifecycle()
    val minLevel by pokemonViewModel.minLevel.collectAsStateWithLifecycle()
    val pokemons by pokemonViewModel.pokemonsState.collectAsStateWithLifecycle()

    var pokemonToDelete by remember { mutableStateOf<PokemonEntity?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        pokemonViewModel.mensaje.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Bolsa de Pokemon",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { pokemonViewModel.updateSearchQuery(it) },
                label = { Text("Buscar por nombre o tipo...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = typeFilter,
                    onValueChange = { pokemonViewModel.updateTypeFilter(it) },
                    label = { Text("Filtro Tipo") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = if (minLevel == 1 && minLevel.toString() == "") "" else minLevel.toString(),
                    onValueChange = {
                        val level = it.toIntOrNull() ?: 1
                        pokemonViewModel.updateMinLevel(level)
                    },
                    label = { Text("Nivel Mínimo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(16.dp))



            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(pokemons) { pokemon ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${pokemon.name} (#${pokemon.number})",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = "Type: ${pokemon.type}")
                                Text(text = "Level: ${pokemon.level}")
                            }

                            IconButton(
                                onClick = { pokemonViewModel.levelUpPokemon(pokemon) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Subir nivel",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(
                                onClick = { pokemonToDelete = pokemon }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Liberar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    pokemonToDelete?.let { pokemon ->
        AlertDialog(
            onDismissRequest = { pokemonToDelete = null },
            title = { Text("Liberar Pokémon") },
            text = { Text("¿Estás seguro de que deseas liberar a ${pokemon.name}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pokemonViewModel.deletePokemon(pokemon)
                        pokemonToDelete = null 
                    }
                ) {
                    Text("Liberar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { pokemonToDelete = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}