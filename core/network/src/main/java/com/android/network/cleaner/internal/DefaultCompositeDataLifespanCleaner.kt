package com.android.network.cleaner.internal

import com.android.network.cleaner.DataLifespanCleaner


internal class DefaultCompositeDataLifespanCleaner(
    private val memoryStorageCleaner: DataLifespanCleaner,
    private val dataStoreCleaner: DataLifespanCleaner,
    private val fileSystemCleaner: DataLifespanCleaner
) : DataLifespanCleaner {

    override suspend fun clearByLogout() {
        memoryStorageCleaner.clearByLogout()
        dataStoreCleaner.clearByLogout()
        fileSystemCleaner.clearByLogout()
    }

    override suspend fun clearByApplicationLaunch() {
        memoryStorageCleaner.clearByApplicationLaunch()
        dataStoreCleaner.clearByApplicationLaunch()
        fileSystemCleaner.clearByApplicationLaunch()
    }

    override suspend fun clearByApplicationForeground() {
        memoryStorageCleaner.clearByApplicationForeground()
        dataStoreCleaner.clearByApplicationForeground()
        fileSystemCleaner.clearByApplicationForeground()
    }
}
