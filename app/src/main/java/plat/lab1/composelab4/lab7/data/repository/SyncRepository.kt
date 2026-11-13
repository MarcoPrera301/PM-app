package plat.lab1.composelab4.lab7.data.repository

import plat.lab1.composelab4.lab7.data.local.CharacterDao
import plat.lab1.composelab4.lab7.data.local.LocationDao
import plat.lab1.composelab4.lab7.data.mappers.toEntity
import plat.lab1.composelab4.lab7.data.remote.RickAndMortyApi

class SyncRepository(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao
) {


    suspend fun syncIfNeeded() {
        val alreadySynced = characterDao.count() > 0 && locationDao.count() > 0
        if (alreadySynced) return

        //descarga d endpoint
        val characters = api.getCharacters()
        val locations = api.getLocations()

        // guarda en room
        characterDao.insertAll(characters.map { it.toEntity() })
        locationDao.insertAll(locations.map { it.toEntity() })
    }
}