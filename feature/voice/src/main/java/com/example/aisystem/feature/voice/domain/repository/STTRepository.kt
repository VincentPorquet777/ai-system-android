package com.example.aisystem.feature.voice.domain.repository

import com.example.aisystem.core.network.api.AiSystemApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class STTRepository @Inject constructor(
    private val api: AiSystemApi
) {
    suspend fun transcribe(audioFile: File): String {
        val requestFile = audioFile.asRequestBody("audio/m4a".toMediaType())
        val body = MultipartBody.Part.createFormData("audio", audioFile.name, requestFile)
        val response = api.transcribe(body)
        return response.transcript
    }
}
