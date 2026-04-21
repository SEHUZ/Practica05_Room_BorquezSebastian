package sebastian.borquez.practica05_room_252115.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sebastian.borquez.practica05_room_252115.data.PokemonEntity
import sebastian.borquez.practica05_room_252115.data.PokemonRepository

class PokemonViewModel(private val repository: PokemonRepository): ViewModel() {

    private val availablePokemons = listOf(
        PokemonEntity(name = "Bulbasaur", number = "001", type = "Grass/Poison"),
        PokemonEntity(name = "Ivysaur", number = "002", type = "Grass/Poison"),
        PokemonEntity(name = "Venusaur", number = "003", type = "Grass/Poison"),
        PokemonEntity(name = "Charmander", number = "004", type = "Fire"),
        PokemonEntity(name = "Charmeleon", number = "005", type = "Fire"),
        PokemonEntity(name = "Charizard", number = "006", type = "Fire/Flying"),
        PokemonEntity(name = "Squirtle", number = "007", type = "Water"),
        PokemonEntity(name = "Wartortle", number = "008", type = "Water"),
        PokemonEntity(name = "Blastoise", number = "009", type = "Water"),
        PokemonEntity(name = "Caterpie", number = "010", type = "Bug"),
        PokemonEntity(name = "Metapod", number = "011", type = "Bug"),
        PokemonEntity(name = "Butterfree", number = "012", type = "Bug/Flying"),
        PokemonEntity(name = "Weedle", number = "013", type = "Bug/Poison"),
        PokemonEntity(name = "Kakuna", number = "014", type = "Bug/Poison"),
        PokemonEntity(name = "Beedrill", number = "015", type = "Bug/Poison"),
        PokemonEntity(name = "Pidgey", number = "016", type = "Normal/Flying"),
        PokemonEntity(name = "Pidgeotto", number = "017", type = "Normal/Flying"),
        PokemonEntity(name = "Pidgeot", number = "018", type = "Normal/Flying"),
        PokemonEntity(name = "Rattata", number = "019", type = "Normal"),
        PokemonEntity(name = "Raticate", number = "020", type = "Normal"),
        PokemonEntity(name = "Spearow", number = "021", type = "Normal/Flying"),
        PokemonEntity(name = "Fearow", number = "022", type = "Normal/Flying"),
        PokemonEntity(name = "Ekans", number = "023", type = "Poison"),
        PokemonEntity(name = "Arbok", number = "024", type = "Poison"),
        PokemonEntity(name = "Pikachu", number = "025", type = "Electric"),
        PokemonEntity(name = "Raichu", number = "026", type = "Electric"),
        PokemonEntity(name = "Sandshrew", number = "027", type = "Ground"),
        PokemonEntity(name = "Sandslash", number = "028", type = "Ground"),
        PokemonEntity(name = "Nidoran♀", number = "029", type = "Poison"),
        PokemonEntity(name = "Nidorina", number = "030", type = "Poison"),
        PokemonEntity(name = "Nidoqueen", number = "031", type = "Poison/Ground"),
        PokemonEntity(name = "Nidoran♂", number = "032", type = "Poison"),
        PokemonEntity(name = "Nidorino", number = "033", type = "Poison"),
        PokemonEntity(name = "Nidoking", number = "034", type = "Poison/Ground"),
        PokemonEntity(name = "Clefairy", number = "035", type = "Fairy"),
        PokemonEntity(name = "Clefable", number = "036", type = "Fairy"),
        PokemonEntity(name = "Vulpix", number = "037", type = "Fire"),
        PokemonEntity(name = "Ninetales", number = "038", type = "Fire"),
        PokemonEntity(name = "Jigglypuff", number = "039", type = "Normal/Fairy"),
        PokemonEntity(name = "Wigglytuff", number = "040", type = "Normal/Fairy"),
        PokemonEntity(name = "Zubat", number = "041", type = "Poison/Flying"),
        PokemonEntity(name = "Golbat", number = "042", type = "Poison/Flying"),
        PokemonEntity(name = "Oddish", number = "043", type = "Grass/Poison"),
        PokemonEntity(name = "Gloom", number = "044", type = "Grass/Poison"),
        PokemonEntity(name = "Vileplume", number = "045", type = "Grass/Poison"),
        PokemonEntity(name = "Paras", number = "046", type = "Bug/Grass"),
        PokemonEntity(name = "Parasect", number = "047", type = "Bug/Grass"),
        PokemonEntity(name = "Venonat", number = "048", type = "Bug/Poison"),
        PokemonEntity(name = "Venomoth", number = "049", type = "Bug/Poison"),
        PokemonEntity(name = "Diglett", number = "050", type = "Ground"),
        PokemonEntity(name = "Dugtrio", number = "051", type = "Ground"),
        PokemonEntity(name = "Meowth", number = "052", type = "Normal"),
        PokemonEntity(name = "Persian", number = "053", type = "Normal"),
        PokemonEntity(name = "Psyduck", number = "054", type = "Water"),
        PokemonEntity(name = "Golduck", number = "055", type = "Water"),
        PokemonEntity(name = "Mankey", number = "056", type = "Fighting"),
        PokemonEntity(name = "Primeape", number = "057", type = "Fighting"),
        PokemonEntity(name = "Growlithe", number = "058", type = "Fire"),
        PokemonEntity(name = "Arcanine", number = "059", type = "Fire"),
        PokemonEntity(name = "Poliwag", number = "060", type = "Water"),
        PokemonEntity(name = "Poliwhirl", number = "061", type = "Water"),
        PokemonEntity(name = "Poliwrath", number = "062", type = "Water/Fighting"),
        PokemonEntity(name = "Abra", number = "063", type = "Psychic"),
        PokemonEntity(name = "Kadabra", number = "064", type = "Psychic"),
        PokemonEntity(name = "Alakazam", number = "065", type = "Psychic"),
        PokemonEntity(name = "Machop", number = "066", type = "Fighting"),
        PokemonEntity(name = "Machoke", number = "067", type = "Fighting"),
        PokemonEntity(name = "Machamp", number = "068", type = "Fighting"),
        PokemonEntity(name = "Bellsprout", number = "069", type = "Grass/Poison"),
        PokemonEntity(name = "Weepinbell", number = "070", type = "Grass/Poison"),
        PokemonEntity(name = "Victreebel", number = "071", type = "Grass/Poison"),
        PokemonEntity(name = "Tentacool", number = "072", type = "Water/Poison"),
        PokemonEntity(name = "Tentacruel", number = "073", type = "Water/Poison"),
        PokemonEntity(name = "Geodude", number = "074", type = "Rock/Ground"),
        PokemonEntity(name = "Graveler", number = "075", type = "Rock/Ground"),
        PokemonEntity(name = "Golem", number = "076", type = "Rock/Ground"),
        PokemonEntity(name = "Ponyta", number = "077", type = "Fire"),
        PokemonEntity(name = "Rapidash", number = "078", type = "Fire"),
        PokemonEntity(name = "Slowpoke", number = "079", type = "Water/Psychic"),
        PokemonEntity(name = "Slowbro", number = "080", type = "Water/Psychic"),
        PokemonEntity(name = "Magnemite", number = "081", type = "Electric/Steel"),
        PokemonEntity(name = "Magneton", number = "082", type = "Electric/Steel"),
        PokemonEntity(name = "Farfetch'd", number = "083", type = "Normal/Flying"),
        PokemonEntity(name = "Doduo", number = "084", type = "Normal/Flying"),
        PokemonEntity(name = "Dodrio", number = "085", type = "Normal/Flying"),
        PokemonEntity(name = "Seel", number = "086", type = "Water"),
        PokemonEntity(name = "Dewgong", number = "087", type = "Water/Ice"),
        PokemonEntity(name = "Grimer", number = "088", type = "Poison"),
        PokemonEntity(name = "Muk", number = "089", type = "Poison"),
        PokemonEntity(name = "Shellder", number = "090", type = "Water"),
        PokemonEntity(name = "Cloyster", number = "091", type = "Water/Ice"),
        PokemonEntity(name = "Gastly", number = "092", type = "Ghost/Poison"),
        PokemonEntity(name = "Haunter", number = "093", type = "Ghost/Poison"),
        PokemonEntity(name = "Gengar", number = "094", type = "Ghost/Poison"),
        PokemonEntity(name = "Onix", number = "095", type = "Rock/Ground"),
        PokemonEntity(name = "Drowzee", number = "096", type = "Psychic"),
        PokemonEntity(name = "Hypno", number = "097", type = "Psychic"),
        PokemonEntity(name = "Krabby", number = "098", type = "Water"),
        PokemonEntity(name = "Kingler", number = "099", type = "Water"),
        PokemonEntity(name = "Voltorb", number = "100", type = "Electric")
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
            val success = (1..100).random()
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
            repository.add(
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
