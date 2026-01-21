package com.example.aisystem.core.network.api.models

data class ChatRequest(
    val message: String,
    val history: List<ChatMessage> = emptyList(),
    val model: String? = null,
    val instructions: String? = null
)

data class ChatMessage(
    val role: String, // "user", "assistant", "developer"
    val content: String
)
