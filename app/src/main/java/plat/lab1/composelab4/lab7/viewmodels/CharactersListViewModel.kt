package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.Character
import plat.lab1.composelab4.lab7.data.UiState
import plat.lab1.composelab4.lab7.data.local.AppDatabase
import plat.lab1.composelab4.lab7.data.repository.CharacterRepository

class CharactersListViewModel(application: Application) : AndroidViewModel(application)  {



    private val repository = CharacterRepository(
        AppDatabase.getInstance(application).characterDao()
    )

    private val _uiState = MutableStateFlow(UiState<List<Character>>())
    val uiState: StateFlow<UiState<List<Character>>> = _uiState.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(4000)
            repository.getCharacters()
                .catch { _uiState.value = UiState(isLoading = false, hasError = true) }
                .collect { characters ->
                    _uiState.value = UiState(isLoading = false, data = characters)
                }
        }
    }

    fun onLoadingClicked() {
        loadJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }

    fun toggleFavoritesFilter() { _showOnlyFavorites.value = !_showOnlyFavorites.value }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch { repository.setFavorite(character.id, !character.isFavorite) }
    }
}