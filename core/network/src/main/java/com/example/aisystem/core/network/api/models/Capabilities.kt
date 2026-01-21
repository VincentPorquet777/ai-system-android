package com.example.aisystem.core.network.api.models

import com.google.gson.annotations.SerializedName

/**
 * Network DTO for capabilities endpoint
 * Maps to common.models.Capabilities domain model
 */
data class CapabilitiesDto(
    val streaming: Boolean,
    val stt: Boolean,
    val tts: Boolean,
    val search: Boolean,
    @SerializedName("model_selection") val modelSelection: Boolean,
    @SerializedName("history_storage") val historyStorage: Boolean,
    @SerializedName("max_message_length") val maxMessageLength: Int,
    @SerializedName("supported_models") val supportedModels: List<String>,
    @SerializedName("api_version") val apiVersion: String,
    @SerializedName("server_time") val serverTime: String
)

// Extension to convert to domain model
fun CapabilitiesDto.toDomain() = com.example.aisystem.core.common.models.Capabilities(
    streaming = streaming,
    stt = stt,
    tts = tts,
    search = search,
    modelSelection = modelSelection,
    historyStorage = historyStorage,
    maxMessageLength = maxMessageLength,
    supportedModels = supportedModels,
    apiVersion = apiVersion,
    serverTime = serverTime
)
