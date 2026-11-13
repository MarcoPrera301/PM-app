package plat.lab1.composelab4.lab7.data.remote.dto

import kotlinx.serialization.Serializable

// El API responde: { "info": {...}, "results": [ ... ] }
// Solo nos interesa "results" (ignoreUnknownKeys ignora el resto).
@Serializable
data class ApiResponseDto<T>(
    val results: List<T>
)

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)
