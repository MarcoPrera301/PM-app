package plat.lab1.composelab4.lab7.data.remote

import plat.lab1.composelab4.lab7.data.remote.dto.CharacterDto
import plat.lab1.composelab4.lab7.data.remote.dto.LocationDto

// La interfaz no sabe nada de Ktor: solo declara qué se puede pedir y qué DTO regresa.
interface RickAndMortyApi {
    suspend fun getCharacters(): List<CharacterDto>
    suspend fun getLocations(): List<LocationDto>
}
