package plat.lab1.composelab4.lab7.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import plat.lab1.composelab4.lab7.Character
import plat.lab1.composelab4.lab7.data.local.CharacterDao
import plat.lab1.composelab4.lab7.data.mappers.toEntity
import plat.lab1.composelab4.lab7.data.mappers.toModel

class CharacterRepository(
    private val characterDao: CharacterDao
) {

    fun getCharacters(): Flow<List<Character>> =
        characterDao.getAll().map { entities -> entities.map { it.toModel() } }

    fun getCharacter(id: Int): Flow<Character?> =
        characterDao.getById(id).map { it?.toModel() }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }

    suspend fun getCharacterOnce(id: Int): Character? =
        characterDao.getById(id).first()?.toModel()

    suspend fun createCharacter(character: Character) {
        characterDao.insert(character.copy(id = 0).toEntity())   // id = 0 -> Room asigna uno
    }

    suspend fun updateCharacter(character: Character) {
        characterDao.update(character.toEntity())
    }

    suspend fun deleteCharacter(id: Int) {
        characterDao.deleteById(id)
    }
}
