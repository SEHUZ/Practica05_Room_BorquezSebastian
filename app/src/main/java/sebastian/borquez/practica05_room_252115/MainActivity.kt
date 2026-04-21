package sebastian.borquez.practica05_room_252115

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sebastian.borquez.practica05_room_252115.data.DataStoreManager
import sebastian.borquez.practica05_room_252115.data.PokemonDatabase
import sebastian.borquez.practica05_room_252115.data.PokemonRepository
import sebastian.borquez.practica05_room_252115.navigation.AppNavigation
import sebastian.borquez.practica05_room_252115.ui.theme.Practica05_Room_BorquezSebastianTheme
import sebastian.borquez.practica05_room_252115.viewModel.AuthViewModel
import sebastian.borquez.practica05_room_252115.viewModel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel = AuthViewModel (DataStoreManager(this))
        val database by lazy { PokemonDatabase.getDatabase(this) }
        val repository by lazy {PokemonRepository(database.pokemonDao())}
        val pokemonViewModel: PokemonViewModel by viewModels { PokemonViewModelFactory(repository)}
        setContent {

            Practica05_Room_BorquezSebastianTheme {
                //MainScreen(viewModel = AuthViewModel(DataStoreManager(this)))
                AppNavigation(authViewModel, pokemonViewModel)


            }
        }
    }

}

class PokemonViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonViewModel(repository) as T

    }

}
