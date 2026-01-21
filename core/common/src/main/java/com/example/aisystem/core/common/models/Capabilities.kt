package com.example.aisystem.core.common.models

data class Capabilities(
    val streaming: Boolean,
    val stt: Boolean,
    val tts: Boolean,
    val search: Boolean,
    val modelSelection: Boolean,
    val historyStorage: Boolean,
    val maxMessageLength: Int,
    val supportedModels: List<String>,
    val apiVersion: String,
    val serverTime: String
)
