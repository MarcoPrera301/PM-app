package plat.lab1.composelab4.lab7.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.CharacterDb
import plat.lab1.composelab4.lab7.Character
import plat.lab1.composelab4.lab7.data.UiState

class CharactersListViewModel : ViewModel() {

    private val characterDb = CharacterDb()

    private val _uiState = MutableStateFlow(UiState<List<Character>>())
    val uiState: StateFlow<UiState<List<Character>>> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(4000) // simula la llamada a internet
            _uiState.value = UiState(
                isLoading = false,
                data = characterDb.getAllCharacters()
            )
        }
    }

    /** Se llama cuando el usuario toca la pantalla mientras está en Loading. */
    fun onLoadingClicked() {
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }
}