package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.UserPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    fun login(name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.saveName(name.trim())
            onSuccess()
        }
    }
}