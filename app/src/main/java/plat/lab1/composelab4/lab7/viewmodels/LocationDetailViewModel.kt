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
import plat.lab1.composelab4.lab7.data.Location
import plat.lab1.composelab4.lab7.data.LocationDb
import plat.lab1.composelab4.lab7.data.UiState
import plat.lab1.composelab4.lab7.navigation.LocationDetailDestination

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationDb = LocationDb()

    private val locationId: Int =
        savedStateHandle.toRoute<LocationDetailDestination>().id

    private val _uiState = MutableStateFlow(UiState<Location>())
    val uiState: StateFlow<UiState<Location>> = _uiState.asStateFlow()

    init {
        loadLocation()
    }

    fun loadLocation() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            delay(2000)
            _uiState.value = UiState(
                isLoading = false,
                data = locationDb.getLocationById(locationId)
            )
        }
    }

    fun onLoadingClicked() {
        _uiState.value = _uiState.value.copy(isLoading = false, hasError = true)
    }
}