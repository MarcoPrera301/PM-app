package plat.lab1.composelab4.lab7.viewmodels


import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.Location
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import plat.lab1.composelab4.lab7.data.LocationFilter
import plat.lab1.composelab4.lab7.data.local.AppDatabase
import plat.lab1.composelab4.lab7.data.repository.LocationRepository
import plat.lab1.composelab4.lab7.data.UiState

class LocationsListViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository = LocationRepository(
        AppDatabase.getInstance(application).locationDao()
    )

    private val _uiState = MutableStateFlow(UiState<List<Location>>())
    val uiState: StateFlow<UiState<List<Location>>> = _uiState.asStateFlow()
    private val _filter = MutableStateFlow(LocationFilter.ALL)
    val filter: StateFlow<LocationFilter> = _filter.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadJob?.cancel()
        loadJob =
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(4000)
            repository.getLocations()
                .catch { _uiState.value = UiState(isLoading = false, hasError = true) }
                .collect { locations ->
                    _uiState.value = UiState(isLoading = false, data = locations)
                }
        }
    }

    fun onLoadingClicked() {
        loadJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }

    fun setFilter(filter: LocationFilter) {
        _filter.value = filter
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            repository.setFavorite(location.id, !location.isFavorite)
        }
    }
}
