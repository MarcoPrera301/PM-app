package plat.lab1.composelab4.lab7.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab1.composelab4.lab7.screens.CharacterDetailScreen
import plat.lab1.composelab4.lab7.screens.CharactersScreen
import plat.lab1.composelab4.lab7.screens.LocationDetailScreen
import plat.lab1.composelab4.lab7.screens.LocationsScreen
import plat.lab1.composelab4.lab7.screens.ProfileScreen

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = innerNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ---- Nested graph de Characters ----
            navigation<CharactersGraph>(startDestination = CharactersListDestination) {
                composable<CharactersListDestination> {
                    CharactersScreen(
                        onCharacterClick = { characterId ->
                            innerNavController.navigate(CharacterDetailDestination(id = characterId))
                        }
                    )
                }
                composable<CharacterDetailDestination> {
                    CharacterDetailScreen(
                        onBackClick = { innerNavController.popBackStack() }
                    )
                }
            }

            // ---- Nested graph de Locations ----
            navigation<LocationsGraph>(startDestination = LocationsListDestination) {
                composable<LocationsListDestination> {
                    LocationsScreen(
                        onLocationClick = { locationId ->
                            innerNavController.navigate(LocationDetailDestination(id = locationId))
                        }
                    )
                }
                composable<LocationDetailDestination> {
                    LocationDetailScreen(
                        onBackClick = { innerNavController.popBackStack() }
                    )
                }
            }

            // ---- Profile ----
            composable<ProfileDestination> {
                ProfileScreen(onLogoutClick = onLogout)
            }
        }
    }
}

@Composable
private fun AppBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            indicatorColor = MaterialTheme.colorScheme.onPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = MaterialTheme.colorScheme.onPrimary
        )

        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.hasRoute(CharactersGraph::class) } == true,
            onClick = { navController.navigateToTab(CharactersGraph) },
            icon = { Icon(Icons.Filled.People, contentDescription = "Characters") },
            label = { Text("Characters") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.hasRoute(LocationsGraph::class) } == true,
            onClick = { navController.navigateToTab(LocationsGraph) },
            icon = { Icon(Icons.Filled.Public, contentDescription = "Locations") },
            label = { Text("Locations") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentDestination?.hasRoute(ProfileDestination::class) == true,
            onClick = { navController.navigateToTab(ProfileDestination) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = itemColors
        )
    }
}

private fun NavHostController.navigateToTab(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}