package com.example.aisystem.feature.voice.domain.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.aisystem.core.storage.db.dao.AudioCacheDao
import com.example.aisystem.core.storage.db.entities.AudioCacheEntity
import com.example.aisystem.core.storage.files.AudioFileManager
import com.example.aisystem.feature.voice.domain.repository.TTSRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class AudioPlaybackState {
    object Idle : AudioPlaybackState()
    data class Loading(val messageId: String) : AudioPlaybackState()
    data class Playing(val messageId: String) : AudioPlaybackState()
}

@Singleton
class AudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioCacheDao: AudioCacheDao,
    private val audioFileManager: AudioFileManager
) {
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _playbackState = MutableStateFlow<AudioPlaybackState>(AudioPlaybackState.Idle)
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    _playbackState.value = AudioPlaybackState.Idle
                }
            }
        })
    }

    suspend fun playMessage(messageId: String, text: String, ttsRepository: TTSRepository) {
        stop() // Stop current playback

        _playbackState.value = AudioPlaybackState.Loading(messageId)

        // Check cache
        val cachedAudio = audioCacheDao.getAudioCache(messageId)
        val audioFile = if (cachedAudio != null) {
            audioFileManager.getAudioFile(messageId)
        } else {
            // Fetch from backend
            try {
                val file = ttsRepository.synthesize(text, messageId)
                audioCacheDao.insert(
                    AudioCacheEntity(
                        messageId = messageId,
                        filePath = file.absolutePath,
                        sizeBytes = file.length()
                    )
                )
                file
            } catch (e: Exception) {
                _playbackState.value = AudioPlaybackState.Idle
                throw e
            }
        }

        // Play
        val mediaItem = MediaItem.fromUri(Uri.fromFile(audioFile))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        _playbackState.value = AudioPlaybackState.Playing(messageId)
    }

    fun stop() {
        exoPlayer.stop()
        _playbackState.value = AudioPlaybackState.Idle
    }

    fun release() {
        exoPlayer.release()
    }
}
