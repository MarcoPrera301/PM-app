package plat.lab1.composelab4.lab7.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.Location
import plat.lab1.composelab4.lab7.data.UiState
import plat.lab1.composelab4.lab7.navigation.LocationDetailDestination
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import plat.lab1.composelab4.lab7.data.local.AppDatabase
import plat.lab1.composelab4.lab7.data.repository.LocationRepository

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = LocationRepository(
        AppDatabase.getInstance(application).locationDao()
    )
    private val locationId: Int =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val _uiState = MutableStateFlow(UiState<Location>())
    val uiState: StateFlow<UiState<Location>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadJob?.cancel()
        loadJob =
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(2000)
            repository.getLocation(locationId)
                .catch { _uiState.value = UiState(isLoading = false, hasError = true) }
                .collect { location ->
                    _uiState.value = UiState(
                        isLoading = false,
                        data = location,
                        hasError = location == null
                    )
                }
        }
    }

    fun onLoadingClicked() {
        loadJob?.cancel()

        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }

    fun toggleFavorite() {
        val current = _uiState.value.data ?: return
        viewModelScope.launch {
            repository.setFavorite(current.id, !current.isFavorite)
        }
    }
}