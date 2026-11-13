package plat.lab1.composelab4.lab7.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import plat.lab1.composelab4.lab7.data.remote.dto.ApiResponseDto
import plat.lab1.composelab4.lab7.data.remote.dto.CharacterDto
import plat.lab1.composelab4.lab7.data.remote.dto.LocationDto

class KtorRickAndMortyApi(
    private val client: HttpClient
) : RickAndMortyApi {

    override suspend fun getCharacters(): List<CharacterDto> {
        val response: ApiResponseDto<CharacterDto> = client.get("character").body()
        return response.results
    }

    override suspend fun getLocations(): List<LocationDto> {
        val response: ApiResponseDto<LocationDto> = client.get("location").body()
        return response.results
    }
}
