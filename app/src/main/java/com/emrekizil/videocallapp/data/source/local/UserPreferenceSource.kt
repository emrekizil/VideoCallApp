package com.emrekizil.videocallapp.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveUsername(username:String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    fun getUsername() : Flow<String> =
        dataStore.data.catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.USERNAME] ?: ""
        }

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
    }
}