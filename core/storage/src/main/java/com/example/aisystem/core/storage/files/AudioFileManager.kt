package com.example.aisystem.core.storage.files

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val audioDir = File(context.cacheDir, "audio").apply { mkdirs() }

    fun getAudioFile(messageId: String): File {
        return File(audioDir, "$messageId.mp3")
    }

    suspend fun saveAudio(messageId: String, responseBody: ResponseBody): File {
        val file = getAudioFile(messageId)
        withContext(Dispatchers.IO) {
            file.outputStream().use { output ->
                responseBody.byteStream().copyTo(output)
            }
        }
        return file
    }

    fun deleteAudio(messageId: String) {
        getAudioFile(messageId).delete()
    }

    fun deleteOldFiles(olderThanMillis: Long) {
        audioDir.listFiles()?.forEach { file ->
            if (file.lastModified() < olderThanMillis) {
                file.delete()
            }
        }
    }

    fun getCacheSizeBytes(): Long {
        return audioDir.listFiles()?.sumOf { it.length() } ?: 0L
    }
}
