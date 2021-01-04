package com.example.e_commerceapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface PreferenceStorage {

    val authToken : Flow<String?>

    suspend fun saveAuthToken(authToken: String)

    suspend fun clearPreferenceStorage()
}

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) : PreferenceStorage {

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = "UserPreferences")

    private object PreferencesKey {
        val AUTH_TOKEN = preferencesKey<String>("auth_token")
    }
    override val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKey.AUTH_TOKEN]
        }

    override suspend fun saveAuthToken(authToken: String) {
        dataStore.setValue(PreferencesKey.AUTH_TOKEN, authToken)
    }

    override suspend fun clearPreferenceStorage() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

}