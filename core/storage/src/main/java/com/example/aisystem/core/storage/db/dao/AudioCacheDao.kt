package com.example.aisystem.core.storage.db.dao

import androidx.room.*
import com.example.aisystem.core.storage.db.entities.AudioCacheEntity

@Dao
interface AudioCacheDao {
    @Query("SELECT * FROM audio_cache WHERE messageId = :messageId")
    suspend fun getAudioCache(messageId: String): AudioCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audioCache: AudioCacheEntity)

    @Delete
    suspend fun delete(audioCache: AudioCacheEntity)

    @Query("DELETE FROM audio_cache WHERE createdAt < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)

    @Query("SELECT COUNT(*) FROM audio_cache")
    suspend fun getCount(): Int
}
