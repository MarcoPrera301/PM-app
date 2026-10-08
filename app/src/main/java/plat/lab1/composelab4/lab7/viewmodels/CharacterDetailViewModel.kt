package plat.lab1.composelab4.lab7.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.Character
import plat.lab1.composelab4.lab7.CharacterDb
import plat.lab1.composelab4.lab7.data.UiState
import plat.lab1.composelab4.lab7.navigation.CharacterDetailDestination

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterDb = CharacterDb()

    // Antes esto llegaba como parámetro de función (characterId: Int) desde
    // el composable. Ahora el propio ViewModel lo saca de la navegación.
    private val characterId: Int =
        savedStateHandle.toRoute<CharacterDetailDestination>().id

    private val _uiState = MutableStateFlow(UiState<Character>())
    val uiState: StateFlow<UiState<Character>> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(2000)
            _uiState.value = UiState(
                isLoading = false,
                data = characterDb.getCharacterById(characterId)
            )
        }
    }

    fun onLoadingClicked() {
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }
}