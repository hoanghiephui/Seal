package com.android.network.internal

import com.android.network.BuildConfig
import com.android.network.HttpCachePool
import com.android.network.internal.interceptor.CacheControlInterceptor
import com.android.network.internal.interceptor.OverrideTimeoutInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val DefaultConnectTimeoutSeconds = 5L
private const val DefaultReadTimeoutSeconds = 10L

internal suspend fun Retrofit.Builder.applyDefaultSetting(createOkHttpClient: suspend () -> OkHttpClient): Retrofit.Builder {
    val contentType = "application/json".toMediaType()
    val json = Json {
        ignoreUnknownKeys = true
    }
    return this
        .addConverterFactory(json.asConverterFactory(contentType))
        .callFactory(TimeoutCallFactory(createOkHttpClient()))
}

internal suspend fun OkHttpClient.Builder.appplyDefaultSetting(
    httpCachePool: HttpCachePool
): OkHttpClient.Builder {
    val httpLoggingInterceptor = if (!BuildConfig.DEBUG) {
        HttpLoggingInterceptor()
    } else {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    return this
        .cache(httpCachePool.getOrCreateCache())
        .connectTimeout(DefaultConnectTimeoutSeconds, TimeUnit.SECONDS)
        .readTimeout(DefaultReadTimeoutSeconds, TimeUnit.SECONDS)
        .addInterceptor(OverrideTimeoutInterceptor())
        .addInterceptor(CacheControlInterceptor())
        .addInterceptor(httpLoggingInterceptor)
}
