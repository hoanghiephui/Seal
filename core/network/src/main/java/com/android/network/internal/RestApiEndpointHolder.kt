package com.android.network.internal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class RestApiEndpointHolder {

    private val mutex = Mutex()
    private val restApiEndpointMap = mutableMapOf<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> getOrCreateEndpoint(
        endpointClass: Class<T>,
        createRestApiEndpoint: suspend (Class<T>) -> T
    ): T {
        return mutex.withLock {
            restApiEndpointMap.getOrPut(endpointClass) {
                createRestApiEndpoint(endpointClass) as Any
            } as T
        }
    }
}
