package com.example.aisystem.feature.chat.domain.model

import com.example.aisystem.core.storage.db.entities.MessageEntity

data class Message(
    val id: String,
    val conversationId: String,
    val role: String, // "user", "assistant"
    val content: String,
    val timestamp: Long,
    val status: MessageStatus,
    val errorMessage: String? = null
)

enum class MessageStatus {
    PENDING,
    SENT,
    FAILED
}

// Extension functions for mapping
fun MessageEntity.toDomain() = Message(
    id = id,
    conversationId = conversationId,
    role = role,
    content = content,
    timestamp = timestamp,
    status = when (status) {
        "PENDING" -> MessageStatus.PENDING
        "SENT" -> MessageStatus.SENT
        "FAILED" -> MessageStatus.FAILED
        else -> MessageStatus.FAILED
    },
    errorMessage = errorMessage
)

fun Message.toEntity() = MessageEntity(
    id = id,
    conversationId = conversationId,
    role = role,
    content = content,
    timestamp = timestamp,
    status = status.name,
    errorMessage = errorMessage
)
