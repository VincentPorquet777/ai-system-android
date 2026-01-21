package com.example.aisystem.core.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aisystem.core.storage.db.dao.AudioCacheDao
import com.example.aisystem.core.storage.db.dao.ConversationDao
import com.example.aisystem.core.storage.db.dao.MessageDao
import com.example.aisystem.core.storage.db.entities.AudioCacheEntity
import com.example.aisystem.core.storage.db.entities.ConversationEntity
import com.example.aisystem.core.storage.db.entities.MessageEntity

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        AudioCacheEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AiSystemDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun audioCacheDao(): AudioCacheDao
}
