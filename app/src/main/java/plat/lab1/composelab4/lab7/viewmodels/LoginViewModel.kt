package plat.lab1.composelab4.lab7.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.composelab4.lab7.data.UserPreferences
import plat.lab1.composelab4.lab7.data.local.AppDatabase
import plat.lab1.composelab4.lab7.data.remote.HttpClientFactory
import plat.lab1.composelab4.lab7.data.remote.KtorRickAndMortyApi
import plat.lab1.composelab4.lab7.data.repository.SyncRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val database = AppDatabase.getInstance(application)

    // El cliente se crea una sola vez y se cierra cuando el ViewModel se destruye.
    private val httpClient = HttpClientFactory.create()

    private val syncRepository = SyncRepository(
        api = KtorRickAndMortyApi(httpClient),
        characterDao = database.characterDao(),
        locationDao = database.locationDao()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    fun login(name: String, onSuccess: () -> Unit) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false

            val success = try {
                syncRepository.syncIfNeeded()
                userPreferences.saveName(name.trim())
                true
            } catch (e: CancellationException) {
                throw e          // no tragarse la cancelación de la corrutina
            } catch (e: Exception) {
                false            // sin internet, timeout, error del servidor, etc.
            } finally {
                _isLoading.value = false
            }

            if (success) onSuccess() else _hasError.value = true
        }
    }

    override fun onCleared() {
        httpClient.close()
        super.onCleared()
    }
}