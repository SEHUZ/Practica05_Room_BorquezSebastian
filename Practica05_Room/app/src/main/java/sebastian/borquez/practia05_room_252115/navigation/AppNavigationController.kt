package sebastian.borquez.practia05_room_252115.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sebastian.borquez.practia05_room_252115.screens.BolsaScreen
import sebastian.borquez.practia05_room_252115.screens.CapturarScreen
import sebastian.borquez.practia05_room_252115.screens.HomeScreen
import sebastian.borquez.practia05_room_252115.screens.LoginScreen
import sebastian.borquez.practia05_room_252115.viewModel.AuthViewModel
import sebastian.borquez.practia05_room_252115.viewModel.PokemonViewModel


sealed class Screen (val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Bolsa : Screen("bolsa")
    object Capturar : Screen("capturar")
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    pokemonViewModel: PokemonViewModel
) {
    val navController = rememberNavController()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val userName by authViewModel.username.collectAsState()

    LaunchedEffect(
        isLoggedIn

    ) {
        if(isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0){inclusive = true}
            }
        }else{
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route) {
        composable(Screen.Login.route){
            LoginScreen(viewModel = authViewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(userName,
                onLogout = {
                    authViewModel.logout()
                },
                onBolsaClick = {navController.navigate(Screen.Bolsa.route)},
                onCapturarClick = {navController.navigate(Screen.Capturar.route)}
            )
        }

        composable(Screen.Bolsa.route) {
            BolsaScreen(pokemonViewModel)
        }

        composable(Screen.Capturar.route) {
            CapturarScreen(pokemonViewModel, onBack = {navController.popBackStack() })
        }

    }
}