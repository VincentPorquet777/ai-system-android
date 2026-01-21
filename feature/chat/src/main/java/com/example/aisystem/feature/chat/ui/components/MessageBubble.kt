package com.example.aisystem.feature.chat.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aisystem.feature.chat.domain.model.Message
import com.example.aisystem.feature.chat.domain.model.MessageStatus
import com.example.aisystem.feature.voice.domain.player.AudioPlaybackState

@Composable
fun MessageBubble(
    message: Message,
    showAudioButton: Boolean,
    audioPlaybackState: AudioPlaybackState,
    onPlayAudio: () -> Unit,
    onStopAudio: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlaying = audioPlaybackState is AudioPlaybackState.Playing &&
            audioPlaybackState.messageId == message.id
    val isLoading = audioPlaybackState is AudioPlaybackState.Loading &&
            audioPlaybackState.messageId == message.id

    val isUserMessage = message.role == "user"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUserMessage) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 1.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Message content
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUserMessage) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                // Status indicator for user messages
                when {
                    message.status == MessageStatus.PENDING -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Sending...",
                            fontSize = 12.sp,
                            color = if (isUserMessage) {
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            } else {
                                Color.Gray
                            }
                        )
                    }
                    message.status == MessageStatus.FAILED -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Failed to send",
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                        TextButton(
                            onClick = onRetry,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Retry", fontSize = 12.sp)
                        }
                    }
                }

                // Audio button for assistant messages
                if (showAudioButton && !isUserMessage) {
                    Spacer(modifier = Modifier.height(4.dp))
                    IconButton(
                        onClick = { if (isPlaying) onStopAudio() else onPlayAudio() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        when {
                            isLoading -> CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            isPlaying -> Icon(
                                Icons.Default.Stop,
                                contentDescription = "Stop",
                                modifier = Modifier.size(20.dp)
                            )
                            else -> Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
