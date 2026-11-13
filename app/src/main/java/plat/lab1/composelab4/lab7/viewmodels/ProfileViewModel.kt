package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.UserPreferences
import plat.lab1.composelab4.lab7.data.local.AppDatabase
import plat.lab1.composelab4.lab7.data.repository.LocalDataRepository
import plat.lab1.composelab4.lab7.data.repository.SyncRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val database = AppDatabase.getInstance(application)
    private val localDataRepository = LocalDataRepository(
        characterDao = database.characterDao(),
        locationDao = database.locationDao()
    )


    val name: StateFlow<String> = userPreferences.name
        .map { it ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearName()
            localDataRepository.clearAll()
            onLoggedOut()
        }
    }
}