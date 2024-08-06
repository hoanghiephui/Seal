package com.android.network.cleaner

import com.android.network.cleaner.internal.DefaultCompositeDataLifespanCleaner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A data cleaner, data will clean related by [DataLifespan]
 */
interface DataLifespanCleaner {

    /**
     * clean data of marked as [DataLifespan.UntilLogout]
     */
    suspend fun clearByLogout()

    /**
     * clean data of marked as [DataLifespan.UntilNextApplicationLaunch]
     */
    suspend fun clearByApplicationLaunch()

    /**
     * clean data of marked as [DataLifespan.UntilNextApplicationForeground]
     */
    suspend fun clearByApplicationForeground()
}

// Suppress documenting lint rule, because this class don't want to visible at document
@Suppress("detekt.UndocumentedPublicClass")
@Module
@InstallIn(SingletonComponent::class)
object DataLifespanCleanerModule {

    @Suppress("detekt.UndocumentedPublicFunction")
    @CompositeDataLifespanCleaner
    @Provides
    @Singleton
    fun provideDataLifespanCleaner(
        @MemoryStorageCleaner
        memoryStorageCleaner: DataLifespanCleaner,
        @DataStoreCleaner
        dataStoreCleaner: DataLifespanCleaner,
        @FileSystemCleaner
        fileSystemCleaner: DataLifespanCleaner
    ): DataLifespanCleaner {
        return DefaultCompositeDataLifespanCleaner(
            memoryStorageCleaner = memoryStorageCleaner,
            dataStoreCleaner = dataStoreCleaner,
            fileSystemCleaner = fileSystemCleaner
        )
    }
}
