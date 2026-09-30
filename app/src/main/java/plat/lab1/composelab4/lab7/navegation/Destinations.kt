package plat.lab1.composelab4.lab7.navigation

import kotlinx.serialization.Serializable

// Graph principal
@Serializable
data object LoginDestination

@Serializable
data object MainDestination

// graph de Characters
@Serializable
data object CharactersGraph

@Serializable
data object CharactersListDestination

@Serializable
data class CharacterDetailDestination(val id: Int)

// graph de Locations
@Serializable
data object LocationsGraph

@Serializable
data object LocationsListDestination

@Serializable
data class LocationDetailDestination(val id: Int)

// Profile
@Serializable
data object ProfileDestination