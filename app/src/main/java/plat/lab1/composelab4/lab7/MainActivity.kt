package plat.lab1.composelab4.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import plat.lab1.composelab4.lab7.navigation.LoginDestination
import plat.lab1.composelab4.lab7.navigation.MainDestination
import plat.lab1.composelab4.lab7.navigation.MainScreen
import plat.lab1.composelab4.lab7.screens.LoginScreen
import plat.lab1.composelab4.lab7.ui.theme.RickMortyTheme
import plat.lab1.composelab4.lab7.navigation.SplashDestination
import plat.lab1.composelab4.lab7.screens.SplashScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickMortyTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = SplashDestination,
                    modifier = Modifier.fillMaxSize()
                ) {

                    composable<SplashDestination> {
                        SplashScreen(
                            onLogged = {
                                navController.navigate(MainDestination) {
                                    popUpTo(SplashDestination) { inclusive = true }
                                }
                            },
                            onNotLogged = {
                                navController.navigate(LoginDestination) {
                                    popUpTo(SplashDestination) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<LoginDestination> {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(MainDestination) {
                                    popUpTo(LoginDestination) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<MainDestination> {
                        MainScreen(
                            onLogout = {
                                navController.navigate(LoginDestination) {
                                    popUpTo(MainDestination) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}