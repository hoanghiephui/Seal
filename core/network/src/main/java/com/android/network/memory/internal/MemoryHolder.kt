package com.android.network.memory.internal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MemoryHolder<T>(defaultValue: T) {

    private val mutex = Mutex()
    private var value = defaultValue

    suspend fun updateData(transform: suspend (T) -> T) {
        mutex.withLock {
            value = transform(value)
        }
    }

    suspend fun getData(): T {
        return mutex.withLock { value }
    }
}
