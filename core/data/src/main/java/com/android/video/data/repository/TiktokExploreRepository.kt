package com.android.video.data.repository

import com.android.video.model.TiktokExploreResponse

interface TiktokExploreRepository {
    suspend fun getExploreVideos(
        webIdLastTime: Long,
        appLanguage: String,
        screenHeight: Int,
        screenWidth: Int,
        region: String,
        language: String
    ): TiktokExploreResponse
}
