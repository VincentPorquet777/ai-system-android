package com.example.aisystem.core.storage.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val encryptedPrefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                "auth_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // Fallback to standard SharedPreferences if encrypted prefs fail
            context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        }
    }

    private val _token = MutableStateFlow<String?>(encryptedPrefs.getString("token", null))
    val token: StateFlow<String?> = _token.asStateFlow()

    fun setToken(token: String) {
        encryptedPrefs.edit().putString("token", token).apply()
        _token.value = token
    }

    fun clearToken() {
        encryptedPrefs.edit().remove("token").apply()
        _token.value = null
    }
}
