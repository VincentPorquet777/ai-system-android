package com.example.aisystem.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aisystem.core.common.models.Capabilities
import com.example.aisystem.core.storage.datastore.CapabilitiesDataStore
import com.example.aisystem.feature.chat.domain.model.Message
import com.example.aisystem.feature.chat.domain.repository.ChatRepository
import com.example.aisystem.feature.chat.domain.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val capabilitiesDataStore: CapabilitiesDataStore,
    private val audioPlayer: com.example.aisystem.feature.voice.domain.player.AudioPlayer,
    private val ttsRepository: com.example.aisystem.feature.voice.domain.repository.TTSRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    val capabilities: StateFlow<Capabilities?> = capabilitiesDataStore.capabilities.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    // Audio playback state
    val audioPlaybackState: StateFlow<AudioPlaybackState> = audioPlayer.playbackState

    private var currentConversationId: String? = null

    fun loadConversation(conversationId: String) {
        currentConversationId = conversationId
        viewModelScope.launch {
            chatRepository.getMessages(conversationId).collect { messages ->
                _messages.value = messages
            }
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val text = _inputText.value.trim()
        val conversationId = currentConversationId ?: return

        if (text.isBlank() || _isSending.value) return

        _inputText.value = ""
        _isSending.value = true

        viewModelScope.launch {
            when (chatRepository.sendMessage(conversationId, text)) {
                is Result.Success -> {
                    // Message sent successfully
                }
                is Result.Error -> {
                    // Error handled by repository (message marked as FAILED)
                }
            }
            _isSending.value = false
        }
    }

    fun retryMessage(messageId: String) {
        viewModelScope.launch {
            _isSending.value = true
            chatRepository.retryMessage(messageId)
            _isSending.value = false
        }
    }

    // Voice methods
    fun playAudio(messageId: String) {
        val message = _messages.value.find { it.id == messageId } ?: return

        viewModelScope.launch {
            try {
                audioPlayer.playMessage(messageId, message.content, ttsRepository)
            } catch (e: Exception) {
                // Handle error (TTS may return 501)
            }
        }
    }

    fun stopAudio() {
        audioPlayer.stop()
    }

    fun startVoiceInput() {
        // TODO: Implement voice input UI/dialog
    }
}
