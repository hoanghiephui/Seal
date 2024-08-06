package com.android.network.memory


/**
 * A in-memory store related to certain [DataLifespan]
 */
interface MemoryStorage<T> {

    suspend fun updateData(transform: suspend (T) -> T)

    /**
     * get the data
     */
    suspend fun getData(): T
}
