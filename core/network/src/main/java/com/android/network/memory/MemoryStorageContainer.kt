package com.android.network.memory


import com.android.network.cleaner.DataLifespanCleaner
import com.android.network.cleaner.MemoryStorageCleaner
import com.android.network.memory.internal.DefaultMemoryStorageContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.properties.ReadOnlyProperty


interface MemoryStorageContainer {

    /**
     * return the [MemoryStorage] related to [dataLifespan].
     */
    fun <TValue> memoryStorage(
        dataLifespan: DataLifespan,
        defaultValue: () -> TValue
    ): ReadOnlyProperty<Any, MemoryStorage<TValue>>
}

@Module
@InstallIn(SingletonComponent::class)
object MemoryStorageContainerModule {

    @Provides
    @Singleton
    fun provideMemoryStorageContainer(): MemoryStorageContainer {
        return DefaultMemoryStorageContainer()
    }

    @MemoryStorageCleaner
    @Provides
    @Singleton
    fun provideMemoryStorageCleaner(memoryStorageContainer: MemoryStorageContainer): DataLifespanCleaner {
        return memoryStorageContainer as DefaultMemoryStorageContainer
    }
}
