package sebastian.borquez.practia05_room_252115

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import sebastian.borquez.practia05_room_252115.data.DataStoreManager
import sebastian.borquez.practia05_room_252115.screens.MainScreen
import sebastian.borquez.practia05_room_252115.ui.theme.BasicLocalStoreTheme
import sebastian.borquez.practia05_room_252115.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val prefs = PreferenceManager(this)
            //var isLoggedIn by remember { mutableStateOf(prefs.isLoggedIn()) }

            BasicLocalStoreTheme {
                MainScreen(viewModel = AuthViewModel(DataStoreManager(this)))

                /* if(isLoggedIn)
                     HomeScreen({
                         prefs.logout()
                         isLoggedIn = false
                     })
                 else
                     LoginScreen({
                         prefs.saveLoginStatus(true)
                         isLoggedIn = true
                     })*/
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicLocalStoreTheme {
        Greeting("Android")
    }
}