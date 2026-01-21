package com.example.aisystem.core.storage.di

import android.content.Context
import androidx.room.Room
import com.example.aisystem.core.storage.db.AiSystemDatabase
import com.example.aisystem.core.storage.db.dao.AudioCacheDao
import com.example.aisystem.core.storage.db.dao.ConversationDao
import com.example.aisystem.core.storage.db.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AiSystemDatabase {
        return Room.databaseBuilder(
            context,
            AiSystemDatabase::class.java,
            "ai_system_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideConversationDao(database: AiSystemDatabase): ConversationDao {
        return database.conversationDao()
    }

    @Provides
    @Singleton
    fun provideMessageDao(database: AiSystemDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    @Singleton
    fun provideAudioCacheDao(database: AiSystemDatabase): AudioCacheDao {
        return database.audioCacheDao()
    }
}
