package com.example.aisystem.core.network.di

import com.example.aisystem.core.network.api.AiSystemApi
import com.example.aisystem.core.network.interceptors.AuthInterceptor
import com.example.aisystem.core.network.interceptors.ClientHeadersInterceptor
import com.example.aisystem.core.storage.datastore.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        clientHeadersInterceptor: ClientHeadersInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(clientHeadersInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        settingsDataStore: SettingsDataStore
    ): Retrofit {
        // Base URL from settings (default to emulator localhost for testing)
        val baseUrl = runBlocking {
            settingsDataStore.baseUrl.first() ?: "http://10.0.2.2:8080/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAiSystemApi(retrofit: Retrofit): AiSystemApi {
        return retrofit.create(AiSystemApi::class.java)
    }
}
