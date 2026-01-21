package com.example.aisystem.core.network.api.models

data class TTSRequest(
    val text: String,
    val voice: String = "alloy",
    val format: String = "mp3"
)
