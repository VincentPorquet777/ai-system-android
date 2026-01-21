package com.example.aisystem.core.storage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.aisystem.core.common.models.Capabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.capabilitiesDataStore: DataStore<Preferences> by preferencesDataStore(name = "capabilities")

@Singleton
class CapabilitiesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val STREAMING = booleanPreferencesKey("streaming")
        val STT = booleanPreferencesKey("stt")
        val TTS = booleanPreferencesKey("tts")
        val SEARCH = booleanPreferencesKey("search")
        val MODEL_SELECTION = booleanPreferencesKey("model_selection")
        val HISTORY_STORAGE = booleanPreferencesKey("history_storage")
        val MAX_MESSAGE_LENGTH = intPreferencesKey("max_message_length")
        val API_VERSION = stringPreferencesKey("api_version")
        val SERVER_TIME = stringPreferencesKey("server_time")
    }

    val capabilities: Flow<Capabilities?> = context.capabilitiesDataStore.data
        .map { preferences ->
            if (preferences[Keys.STREAMING] == null) {
                null
            } else {
                Capabilities(
                    streaming = preferences[Keys.STREAMING] ?: false,
                    stt = preferences[Keys.STT] ?: false,
                    tts = preferences[Keys.TTS] ?: false,
                    search = preferences[Keys.SEARCH] ?: false,
                    modelSelection = preferences[Keys.MODEL_SELECTION] ?: false,
                    historyStorage = preferences[Keys.HISTORY_STORAGE] ?: false,
                    maxMessageLength = preferences[Keys.MAX_MESSAGE_LENGTH] ?: 4000,
                    supportedModels = emptyList(), // We'll skip storing this complex list
                    apiVersion = preferences[Keys.API_VERSION] ?: "1.0.0",
                    serverTime = preferences[Keys.SERVER_TIME] ?: ""
                )
            }
        }

    suspend fun updateCapabilities(caps: Capabilities) {
        context.capabilitiesDataStore.edit { preferences ->
            preferences[Keys.STREAMING] = caps.streaming
            preferences[Keys.STT] = caps.stt
            preferences[Keys.TTS] = caps.tts
            preferences[Keys.SEARCH] = caps.search
            preferences[Keys.MODEL_SELECTION] = caps.modelSelection
            preferences[Keys.HISTORY_STORAGE] = caps.historyStorage
            preferences[Keys.MAX_MESSAGE_LENGTH] = caps.maxMessageLength
            preferences[Keys.API_VERSION] = caps.apiVersion
            preferences[Keys.SERVER_TIME] = caps.serverTime
        }
    }

    suspend fun clearCapabilities() {
        context.capabilitiesDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
