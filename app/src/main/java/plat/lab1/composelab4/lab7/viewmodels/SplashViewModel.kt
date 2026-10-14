package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.UserPreferences

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    //inicia en null antes de validar en el splash destanation
    private val _isLogged = MutableStateFlow<Boolean?>(null)
    val isLogged: StateFlow<Boolean?> = _isLogged.asStateFlow()

    init {
        viewModelScope.launch {
            _isLogged.value = !userPreferences.getName().isNullOrBlank()
        }
    }
}