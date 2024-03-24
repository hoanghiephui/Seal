package com.junkfood.seal.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.data.UserPreferences
import com.data.copy
import com.junkfood.seal.model.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                downloadCount = it.downloadCount,
                makePro = it.makePro,
                lastDay = it.lastDay
            )
        }

    suspend fun setDownloadCount(number: Int) {
        try {
            userPreferences.updateData {
                it.copy {
                    downloadCount = number
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setLastDay(lastDay: Long) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.lastDay = lastDay
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun makePro(isPro: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.makePro = isPro
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }
}