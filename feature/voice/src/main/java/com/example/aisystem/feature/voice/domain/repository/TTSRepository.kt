package com.example.aisystem.feature.voice.domain.repository

import com.example.aisystem.core.network.api.AiSystemApi
import com.example.aisystem.core.network.api.models.TTSRequest
import com.example.aisystem.core.storage.files.AudioFileManager
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TTSRepository @Inject constructor(
    private val api: AiSystemApi,
    private val audioFileManager: AudioFileManager
) {
    suspend fun synthesize(text: String, messageId: String): File {
        val request = TTSRequest(text = text)
        val response = api.synthesize(request)
        // Convert ResponseBody to InputStream in the network layer
        return audioFileManager.saveAudio(messageId, response.byteStream())
    }
}
