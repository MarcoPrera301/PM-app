package plat.lab1.composelab4.lab7.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab1.composelab4.lab7.data.Location
import plat.lab1.composelab4.lab7.data.local.LocationDao
import plat.lab1.composelab4.lab7.data.mappers.toModel

class LocationRepository(
    private val locationDao: LocationDao
) {

    fun getLocations(): Flow<List<Location>> =
        locationDao.getAll().map { entities -> entities.map { it.toModel() } }

    fun getLocation(id: Int): Flow<Location?> =
        locationDao.getById(id).map { it?.toModel() }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        locationDao.updateFavorite(id, isFavorite)
    }
}
