package com.example.aisystem.core.network.api

import com.example.aisystem.core.network.api.models.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface AiSystemApi {
    @GET("capabilities")
    suspend fun getCapabilities(): CapabilitiesDto

    @POST("chat")
    suspend fun chat(@Body request: ChatRequest): ChatResponse

    @POST("chat/stream")
    suspend fun chatStream(@Body request: ChatRequest): ResponseBody

    @Multipart
    @POST("stt")
    suspend fun transcribe(@Part audio: MultipartBody.Part): TranscriptionResponse

    @POST("tts")
    suspend fun synthesize(@Body request: TTSRequest): ResponseBody
}
