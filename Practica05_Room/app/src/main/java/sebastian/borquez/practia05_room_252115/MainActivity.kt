package sebastian.borquez.practia05_room_252115

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sebastian.borquez.practia05_room_252115.data.DataStoreManager
import sebastian.borquez.practia05_room_252115.data.PokemonDatabase
import sebastian.borquez.practia05_room_252115.data.PokemonRepository
import sebastian.borquez.practia05_room_252115.navigation.AppNavigation
import sebastian.borquez.practia05_room_252115.ui.theme.BasicLocalStoreTheme
import sebastian.borquez.practia05_room_252115.viewModel.AuthViewModel
import sebastian.borquez.practia05_room_252115.viewModel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel = AuthViewModel (DataStoreManager(this))
        val database by lazy { PokemonDatabase.getDatabase(this) }
        val repository by lazy {PokemonRepository(database.pokemonDao())}
        val pokemonViewModel: PokemonViewModel by viewModels { PokemonViewModelFactory(repository)}
        setContent {
            //val prefs = PreferenceManager(this)
            //var isLoggedIn by remember { mutableStateOf(prefs.isLoggedIn()) }

            BasicLocalStoreTheme {
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
