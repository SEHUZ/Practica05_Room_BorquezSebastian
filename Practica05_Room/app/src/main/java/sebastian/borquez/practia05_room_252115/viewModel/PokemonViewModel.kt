package sebastian.borquez.practia05_room_252115.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sebastian.borquez.practia05_room_252115.data.PokemonEntity
import sebastian.borquez.practia05_room_252115.data.PokemonRepository

class PokemonViewModel(private val repository: PokemonRepository): ViewModel() {

    private val availablePokemons = listOf(
        PokemonEntity(name = "Pikachu", number = "025", type = "Electric")
    )

    var wildPokemon by mutableStateOf<PokemonEntity?>(null)
        private set

    var capturedPokemons by mutableStateOf(listOf<PokemonEntity>())
        private set

    var pokemonSeEscapo by mutableStateOf(false)
        private set

    fun searchPokemon() {
        wildPokemon = availablePokemons.random()
    }

    fun releaseCapturedPokemons() {
        capturedPokemons = emptyList()
    }

    fun capturePokemon() {
        wildPokemon?.let {
            val success = (1 .. 100).random()
            if (success > 50) {
                capturedPokemons = capturedPokemons + it
                pokemonSeEscapo = false
                wildPokemon = null
            } else {
                pokemonSeEscapo = true
                wildPokemon = null
            }
        }
    }

    val pokemonsState: StateFlow<List<PokemonEntity>> = repository.allPokemons
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPokemon(name: String, number: String, type: String, level: Int = 1) {
        viewModelScope.launch {
            repository.add (
                PokemonEntity(
                    name = name,
                    number = number,
                    type = type,
                    level = level
                )
            )
        }
    }
}