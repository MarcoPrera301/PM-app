package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.Character
import plat.lab1.composelab4.lab7.data.local.AppDatabase
import plat.lab1.composelab4.lab7.data.repository.CharacterRepository
import plat.lab1.composelab4.lab7.navigation.CharacterFormDestination

data class CharacterFormState(
    val name: String = "",
    val status: String = "Alive",
    val species: String = "",
    val gender: String = "unknown",
    val image: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val notFound: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() && species.isNotBlank()
}

class CharacterFormViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(
        AppDatabase.getInstance(application).characterDao()
    )

    // id == 0 -> crear personaje nuevo | id != 0 -> editar ese personaje
    private val characterId: Int =
        savedStateHandle.toRoute<CharacterFormDestination>().id

    val isEditing: Boolean = characterId != 0

    private val _formState = MutableStateFlow(CharacterFormState(isLoading = isEditing))
    val formState: StateFlow<CharacterFormState> = _formState.asStateFlow()

    // Personaje original (solo al editar): de aquí conservamos id e isFavorite
    private var original: Character? = null

    init {
        if (isEditing) loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            val character = repository.getCharacterOnce(characterId)
            if (character == null) {
                _formState.update { it.copy(isLoading = false, notFound = true) }
            } else {
                original = character
                _formState.value = CharacterFormState(
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    gender = character.gender,
                    image = character.image
                )
            }
        }
    }

    fun onNameChange(value: String) = _formState.update { it.copy(name = value) }
    fun onSpeciesChange(value: String) = _formState.update { it.copy(species = value) }
    fun onStatusChange(value: String) = _formState.update { it.copy(status = value) }
    fun onGenderChange(value: String) = _formState.update { it.copy(gender = value) }
    fun onImageChange(value: String) = _formState.update { it.copy(image = value) }

    fun save(onSaved: () -> Unit) {
        val state = _formState.value
        if (!state.isValid || state.isLoading || state.isSaving) return

        _formState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val base = original
            if (isEditing && base != null) {
                repository.updateCharacter(
                    base.copy(
                        name = state.name.trim(),
                        status = state.status,
                        species = state.species.trim(),
                        gender = state.gender,
                        image = state.image.trim()
                    )
                )
            } else {
                repository.createCharacter(
                    Character(
                        id = 0,
                        name = state.name.trim(),
                        status = state.status,
                        species = state.species.trim(),
                        gender = state.gender,
                        image = state.image.trim(),
                        isFavorite = false
                    )
                )
            }
            onSaved()
        }
    }
}
