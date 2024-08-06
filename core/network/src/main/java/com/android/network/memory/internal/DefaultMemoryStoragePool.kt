package com.android.network.memory.internal

import java.util.concurrent.ConcurrentHashMap

internal class DefaultMemoryStoragePool {

    private data class MemoryKey(
        val owner: Any,
        val key: String
    )

    private val memories = ConcurrentHashMap<MemoryKey, MemoryHolder<*>>()

    fun <T> getOrCreateMemory(owner: Any, key: String, defaultValue: T): MemoryHolder<T> {
        val memoryKey = MemoryKey(owner, key)
        @Suppress("UNCHECKED_CAST")
        return memories.getOrPut(memoryKey) {
            MemoryHolder(defaultValue)
        } as MemoryHolder<T>
    }

    fun clear() {
        memories.clear()
    }
}
