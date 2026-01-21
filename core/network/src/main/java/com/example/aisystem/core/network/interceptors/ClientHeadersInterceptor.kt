package com.example.aisystem.core.network.interceptors

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ClientHeadersInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Client", "android")
            .addHeader("X-App-Version", "1.0.0") // TODO: Use BuildConfig.VERSION_NAME
            .addHeader("X-Device", "${Build.MODEL}-Android-${Build.VERSION.SDK_INT}")
            .addHeader("X-Api-Version", "1.0.0")
            .build()

        return chain.proceed(request)
    }
}
