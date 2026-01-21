package com.example.aisystem.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aisystem.feature.settings.viewmodel.ConnectionStatus
import com.example.aisystem.feature.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val baseUrl by viewModel.baseUrl.collectAsState()
    val authToken by viewModel.authToken.collectAsState()
    val connectionStatus by viewModel.connectionStatus.collectAsState()
    val capabilities by viewModel.capabilities.collectAsState()

    var baseUrlInput by remember { mutableStateOf(baseUrl ?: "") }
    var authTokenInput by remember { mutableStateOf(authToken ?: "") }

    LaunchedEffect(baseUrl) {
        baseUrl?.let { baseUrlInput = it }
    }

    LaunchedEffect(authToken) {
        authToken?.let { authTokenInput = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Server URL
        OutlinedTextField(
            value = baseUrlInput,
            onValueChange = {
                baseUrlInput = it
                viewModel.updateBaseUrl(it)
            },
            label = { Text("Backend URL") },
            placeholder = { Text("http://10.0.2.2:8080/") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Auth Token
        OutlinedTextField(
            value = authTokenInput,
            onValueChange = {
                authTokenInput = it
                viewModel.updateAuthToken(it)
            },
            label = { Text("Auth Token (optional)") },
            placeholder = { Text("your-bearer-token") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Test Connection Button
        Button(
            onClick = { viewModel.testConnection() },
            modifier = Modifier.fillMaxWidth(),
            enabled = baseUrlInput.isNotBlank()
        ) {
            Text("Test Connection")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Connection Status
        when (connectionStatus) {
            is ConnectionStatus.Success -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Connected successfully",
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            is ConnectionStatus.Error -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = Color(0xFFF44336)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            "Connection failed",
                            color = Color(0xFFF44336)
                        )
                        Text(
                            (connectionStatus as ConnectionStatus.Error).message,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF44336)
                        )
                    }
                }
            }
            is ConnectionStatus.Loading -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Testing connection...")
                }
            }
            else -> {}
        }

        // Capabilities Display
        capabilities?.let { caps ->
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Server Capabilities:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CapabilityItem("Streaming", caps.streaming)
            CapabilityItem("Speech-to-Text (STT)", caps.stt)
            CapabilityItem("Text-to-Speech (TTS)", caps.tts)
            CapabilityItem("Search", caps.search)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "API Version: ${caps.apiVersion}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CapabilityItem(name: String, enabled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name)
        Text(
            if (enabled) "✓" else "✗",
            color = if (enabled) Color(0xFF4CAF50) else Color(0xFFF44336)
        )
    }
}
