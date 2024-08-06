package com.android.network.internal

import com.android.network.HttpCachePool
import com.android.network.memory.DataLifespan
import com.android.network.memory.MemoryStorageContainer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class RestApiPool @Inject constructor(
    memoryStorageContainer: MemoryStorageContainer,
    private val httpCachePool: HttpCachePool,
) {

    private val retrofitHolderMemoryStorage by memoryStorageContainer.memoryStorage(DataLifespan.UntilNextApplicationForeground) {
        RetrofitHolder()
    }
    private val restApiEndpointHolderMemoryStorage by memoryStorageContainer.memoryStorage(
        DataLifespan.UntilNextApplicationForeground
    ) {
        RestApiEndpointHolder()
    }

    private suspend fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .appplyDefaultSetting(httpCachePool)
            .build()
    }

    private suspend fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.tiktok.com")
            .applyDefaultSetting { createOkHttpClient() }
            .build()
    }

    private suspend fun <T> createEndpoint(endpointClass: Class<T>): T {
        val retrofit = retrofitHolderMemoryStorage.getData()
            .getOrCreateRetrofit { createRetrofit() }
        return retrofit.create(endpointClass)
    }

    suspend fun <T> getOrCreateEndpoint(endpointClass: Class<T>): T {
        return restApiEndpointHolderMemoryStorage.getData()
            .getOrCreateEndpoint(endpointClass) { createEndpoint(endpointClass) }
    }
}
