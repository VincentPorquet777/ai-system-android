package com.example.aisystem.core.storage.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_cache")
data class AudioCacheEntity(
    @PrimaryKey val messageId: String,
    val filePath: String,
    val createdAt: Long = System.currentTimeMillis(),
    val sizeBytes: Long
)
