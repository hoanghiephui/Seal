package com.android.video.data.repository.internal

import com.android.network.RestApiContainer
import com.android.network.service.TiktokService
import com.android.network.service.queryParams
import com.android.video.data.repository.TiktokExploreRepository
import com.android.video.model.TiktokExploreResponse
import javax.inject.Inject

class TiktokRestApi @Inject constructor(
    restApiContainer: RestApiContainer
): TiktokExploreRepository {
    private val restApi by restApiContainer.restApi(TiktokService::class.java)

    override suspend fun getExploreVideos(
        webIdLastTime: Long,
        appLanguage: String,
        screenHeight: Int,
        screenWidth: Int,
        region: String,
        language: String
    ): TiktokExploreResponse {
        return restApi.callApi { getExploreVideos(
            webIdLastTime = webIdLastTime,
            appLanguage = appLanguage,
            screenHeight = screenHeight,
            screenWidth = screenWidth,
            region = region,
            language = language,
            queryParams = queryParams
        ) }
    }
}
