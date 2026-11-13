package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import plat.lab1.composelab4.lab7.navigation.CharacterDetailDestination

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterDb =
        CharacterRepository(AppDatabase.getInstance(application).characterDao())

    private val characterId: Int =
        savedStateHandle.toRoute<CharacterDetailDestination>().id

    private val _uiState = MutableStateFlow(UiState<Character>())
    val uiState: StateFlow<UiState<Character>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    private val repository = CharacterRepository(
        AppDatabase.getInstance(application).characterDao()
    )


    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(2000)
            repository.getCharacter(characterId)
                .catch { _uiState.value = UiState(isLoading = false, hasError = true) }
                .collect { character ->
                    _uiState.value = UiState(
                        isLoading = false,
                        data = character,
                        hasError = character == null
                    )
                }
        }
    }

    fun onLoadingClicked() {
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
        loadJob?.cancel()
    }

    fun toggleFavorite() {
        val current = _uiState.value.data ?: return
        viewModelScope.launch { repository.setFavorite(current.id, !current.isFavorite) }
    }

    fun deleteCharacter(onDeleted: () -> Unit) {
        val current = _uiState.value.data ?: return
        viewModelScope.launch {
            loadJob?.cancel()                          // deja de escuchar a Room antes de borrar
            repository.deleteCharacter(current.id)
            onDeleted()
        }
    }
}