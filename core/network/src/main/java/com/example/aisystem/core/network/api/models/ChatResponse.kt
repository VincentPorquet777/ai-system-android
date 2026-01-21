package com.example.aisystem.core.network.api.models

data class ChatResponse(
    val reply: String,
    val model: String,
    val service: String,
    val revision: String
)
