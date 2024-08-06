package com.android.network.internal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Retrofit

internal class RetrofitHolder {

    private val mutex = Mutex()
    private var retrofit: Retrofit? = null

    suspend fun getOrCreateRetrofit(
        createRetrofit: suspend () -> Retrofit
    ): Retrofit {
        return mutex.withLock {
            retrofit = retrofit ?: createRetrofit()
            checkNotNull(retrofit)
        }
    }
}
