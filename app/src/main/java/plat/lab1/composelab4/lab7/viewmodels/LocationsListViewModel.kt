package plat.lab1.composelab4.lab7.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.Location
import plat.lab1.composelab4.lab7.data.LocationDb
import plat.lab1.composelab4.lab7.data.UiState

class LocationsListViewModel : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(UiState<List<Location>>())
    val uiState: StateFlow<UiState<List<Location>>> = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(4000)
            _uiState.value = UiState(
                isLoading = false,
                data = locationDb.getAllLocations()
            )
        }
    }

    fun onLoadingClicked() {
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }
}
