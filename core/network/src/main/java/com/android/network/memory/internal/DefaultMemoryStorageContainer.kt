package com.android.network.memory.internal

import com.android.network.cleaner.DataLifespanCleaner
import com.android.network.memory.DataLifespan
import com.android.network.memory.MemoryStorage
import com.android.network.memory.MemoryStorageContainer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class DefaultMemoryStorageContainer : MemoryStorageContainer, DataLifespanCleaner {

    private class MemoryStorageReadOnlyProperty<TValue>(
        private val memoryStoragePool: DefaultMemoryStoragePool,
        private val defaultValue: () -> TValue
    ) : ReadOnlyProperty<Any, MemoryStorage<TValue>> {

        override fun getValue(thisRef: Any, property: KProperty<*>): MemoryStorage<TValue> {
            return DefaultMemoryStorage(memoryStoragePool, thisRef, property.name, defaultValue)
        }
    }

    private val untilApplicationUninstallMemoryStoragePool = DefaultMemoryStoragePool()
    private val untilLogoutMemoryStoragePool = DefaultMemoryStoragePool()
    private val untilNextApplicationLaunchMemoryStoragePool = DefaultMemoryStoragePool()
    private val untilNextApplicationForegroundMemoryStoragePool = DefaultMemoryStoragePool()

    override fun <TValue> memoryStorage(
        dataLifespan: DataLifespan,
        defaultValue: () -> TValue
    ): ReadOnlyProperty<Any, MemoryStorage<TValue>> {
        return when (dataLifespan) {
            DataLifespan.UntilApplicationUninstall -> MemoryStorageReadOnlyProperty(
                untilApplicationUninstallMemoryStoragePool,
                defaultValue
            )
            DataLifespan.UntilLogout -> MemoryStorageReadOnlyProperty(
                untilLogoutMemoryStoragePool,
                defaultValue
            )
            DataLifespan.UntilNextApplicationLaunch -> MemoryStorageReadOnlyProperty(
                untilNextApplicationLaunchMemoryStoragePool,
                defaultValue
            )
            DataLifespan.UntilNextApplicationForeground -> MemoryStorageReadOnlyProperty(
                untilNextApplicationForegroundMemoryStoragePool,
                defaultValue
            )
        }
    }

    override suspend fun clearByLogout() {
        untilLogoutMemoryStoragePool.clear()
        untilNextApplicationLaunchMemoryStoragePool.clear()
        untilNextApplicationForegroundMemoryStoragePool.clear()
    }

    override suspend fun clearByApplicationLaunch() {
        untilNextApplicationLaunchMemoryStoragePool.clear()
        untilNextApplicationForegroundMemoryStoragePool.clear()
    }

    override suspend fun clearByApplicationForeground() {
        untilNextApplicationForegroundMemoryStoragePool.clear()
    }
}
