package com.example.aisystem.feature.chat.domain.repository

import com.example.aisystem.core.network.api.AiSystemApi
import com.example.aisystem.core.network.api.models.ChatMessage
import com.example.aisystem.core.network.api.models.ChatRequest
import com.example.aisystem.core.storage.db.dao.ConversationDao
import com.example.aisystem.core.storage.db.dao.MessageDao
import com.example.aisystem.core.storage.db.entities.ConversationEntity
import com.example.aisystem.core.storage.db.entities.MessageEntity
import com.example.aisystem.feature.chat.domain.model.Conversation
import com.example.aisystem.feature.chat.domain.model.Message
import com.example.aisystem.feature.chat.domain.model.MessageStatus
import com.example.aisystem.feature.chat.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

@Singleton
class ChatRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    private val api: AiSystemApi
) {
    fun getConversations(): Flow<List<Conversation>> {
        return conversationDao.getAllConversations()
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getMessages(conversationId: String): Flow<List<Message>> {
        return messageDao.getMessagesByConversation(conversationId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    suspend fun createConversation(title: String): String {
        val conversation = ConversationEntity(title = title)
        conversationDao.insert(conversation)
        return conversation.id
    }

    suspend fun deleteConversation(conversationId: String) {
        val conversation = conversationDao.getConversationById(conversationId)
        conversation?.let {
            conversationDao.delete(it)
        }
    }

    suspend fun sendMessage(conversationId: String, text: String): Result<Unit> {
        // 1. Insert user message (PENDING)
        val userMessage = MessageEntity(
            conversationId = conversationId,
            role = "user",
            content = text,
            status = "PENDING"
        )
        messageDao.insert(userMessage)

        return try {
            // 2. Build history
            val history = messageDao.getMessagesSync(conversationId)
                .filter { it.status == "SENT" }
                .map { ChatMessage(it.role, it.content) }

            // 3. Call API
            val request = ChatRequest(message = text, history = history)
            val response = api.chat(request)

            // 4. Update user message to SENT
            messageDao.update(userMessage.copy(status = "SENT"))

            // 5. Insert assistant message
            val assistantMessage = MessageEntity(
                conversationId = conversationId,
                role = "assistant",
                content = response.reply,
                status = "SENT"
            )
            messageDao.insert(assistantMessage)

            // 6. Update conversation
            val preview = if (response.reply.length > 100) {
                response.reply.substring(0, 100) + "..."
            } else {
                response.reply
            }
            conversationDao.updateLastMessage(conversationId, preview)

            Result.Success(Unit)
        } catch (e: Exception) {
            messageDao.update(userMessage.copy(status = "FAILED", errorMessage = e.message))
            Result.Error(e)
        }
    }

    suspend fun retryMessage(messageId: String): Result<Unit> {
        val message = messageDao.getMessageById(messageId) ?: return Result.Error(
            Exception("Message not found")
        )

        if (message.role != "user" || message.status != "FAILED") {
            return Result.Error(Exception("Can only retry failed user messages"))
        }

        // Delete the failed message and resend
        messageDao.delete(message)
        return sendMessage(message.conversationId, message.content)
    }
}
