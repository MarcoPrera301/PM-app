package plat.lab1.composelab4.lab7.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

//instancia de DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences(context: Context) {

    private val dataStore = context.applicationContext.dataStore

    // nombre y tipo
    private val nameKey = stringPreferencesKey("name")

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[nameKey] = name
        }
    }

    suspend fun clearName() {
        dataStore.edit { preferences ->
            preferences.remove(nameKey)
        }
    }

    // observa el cambio de namekey
    val name: Flow<String?> = dataStore.data.map { preferences -> preferences[nameKey] }

    // getter en datastore
    suspend fun getName(): String? = dataStore.data.first()[nameKey]
}