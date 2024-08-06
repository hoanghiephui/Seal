package com.android.network.memory.internal

import com.android.network.memory.MemoryStorage


internal class DefaultMemoryStorage<T>(
    private val memoryStoragePool: DefaultMemoryStoragePool,
    private val owner: Any,
    private val key: String,
    private val defaultValue: () -> T
) : MemoryStorage<T> {

    override suspend fun getData(): T {
        return memoryStoragePool.getOrCreateMemory(owner, key, defaultValue())
            .getData()
    }

    override suspend fun updateData(transform: suspend (T) -> T) {
        return memoryStoragePool.getOrCreateMemory(owner, key, defaultValue())
            .updateData(transform)
    }
}
