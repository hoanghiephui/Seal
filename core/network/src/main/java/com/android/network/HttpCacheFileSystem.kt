package com.android.network

import android.content.Context
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Cache
import java.io.File

internal class HttpCacheFileSystem(
    private val context: Context
) : HttpCachePool {

    private var instance: Cache? = null

    private val mutex = Mutex()

    override suspend fun getOrCreateCache(): Cache {
        return mutex.withLock {
            instance ?: Cache(
                directory = File(context.cacheDir, "video_cache_dir"),
                maxSize = 50L * 1024L * 1024L // 50 MiB
            ).also {
                instance = it
            }
        }
    }
}
