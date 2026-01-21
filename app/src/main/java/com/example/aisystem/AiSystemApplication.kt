package com.example.aisystem

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AiSystemApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
