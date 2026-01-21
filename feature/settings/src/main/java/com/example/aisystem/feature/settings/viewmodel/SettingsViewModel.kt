package com.example.aisystem.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aisystem.core.common.models.Capabilities
import com.example.aisystem.core.network.api.AiSystemApi
import com.example.aisystem.core.network.api.models.toDomain
import com.example.aisystem.core.storage.datastore.AuthDataStore
import com.example.aisystem.core.storage.datastore.CapabilitiesDataStore
import com.example.aisystem.core.storage.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val authDataStore: AuthDataStore,
    private val capabilitiesDataStore: CapabilitiesDataStore,
    private val api: AiSystemApi
) : ViewModel() {

    val baseUrl: StateFlow<String?> = settingsDataStore.baseUrl.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val authToken: StateFlow<String?> = authDataStore.token

    val capabilities: StateFlow<Capabilities?> = capabilitiesDataStore.capabilities.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Idle)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    fun updateBaseUrl(url: String) {
        viewModelScope.launch {
            settingsDataStore.setBaseUrl(url)
        }
    }

    fun updateAuthToken(token: String) {
        authDataStore.setToken(token)
    }

    fun testConnection() {
        viewModelScope.launch {
            _connectionStatus.value = ConnectionStatus.Loading
            try {
                val capsDto = api.getCapabilities()
                val caps = capsDto.toDomain()
                capabilitiesDataStore.updateCapabilities(caps)
                _connectionStatus.value = ConnectionStatus.Success
            } catch (e: Exception) {
                _connectionStatus.value = ConnectionStatus.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class ConnectionStatus {
    object Idle : ConnectionStatus()
    object Loading : ConnectionStatus()
    object Success : ConnectionStatus()
    data class Error(val message: String) : ConnectionStatus()
}
