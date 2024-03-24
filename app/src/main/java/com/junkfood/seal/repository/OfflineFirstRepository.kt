package com.junkfood.seal.repository

import com.junkfood.seal.datastore.PreferencesDataSource
import com.junkfood.seal.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OfflineFirstRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    suspend fun setDownloadCount(number: Int)

    suspend fun setLastDay(lastDay: Long)

    suspend fun makePro(isPro: Boolean)
}

class OfflineFirstRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : OfflineFirstRepository {
    override val userData: Flow<UserData>
        get() = preferencesDataSource.userData

    override suspend fun setDownloadCount(number: Int) =
        preferencesDataSource.setDownloadCount(number)

    override suspend fun setLastDay(lastDay: Long) =
        preferencesDataSource.setLastDay(lastDay)

    override suspend fun makePro(isPro: Boolean) =
        preferencesDataSource.makePro(isPro)
}