package plat.lab1.composelab4.lab7.data.repository

import plat.lab1.composelab4.lab7.data.local.CharacterDao
import plat.lab1.composelab4.lab7.data.local.LocationDao

/** Operaciones sobre todos los datos locales (se usa al cerrar sesión). */
class LocalDataRepository(
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao
) {

    suspend fun clearAll() {
        characterDao.deleteAll()
        locationDao.deleteAll()
    }
}
