package com.android.network.service

import com.android.video.model.TiktokExploreResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TiktokService {
    @GET("/api/explore/item_list")
    suspend fun getExploreVideos(
        @Query("WebIdLastTime") webIdLastTime: Long,
        @Query("app_language") appLanguage: String,
        @Query("screen_height") screenHeight: Int,
        @Query("screen_width") screenWidth: Int,
        @Query("region") region: String,
        @Query("language") language: String,
        @QueryMap queryParams: Map<String, String>
    ): TiktokExploreResponse
}


val queryParams = mapOf(
    "aid" to "1988",
    "app_name" to "tiktok_web",
    "browser_language" to "en-GB",
    "browser_name" to "Mozilla",
    "browser_online" to "true",
    "browser_platform" to "MacIntel",
    "browser_version" to "5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.6 Safari/605.1.15",
    "categoryType" to "120",
    "channel" to "tiktok_web",
    "cookie_enabled" to "true",
    "count" to "16",
    "data_collection_enabled" to "true",
    "device_id" to "7379830111291622930",
    "device_platform" to "web_pc",
    "focus_state" to "true",
    "from_page" to "",
    "history_len" to "3",
    "is_fullscreen" to "false",
    "is_page_visible" to "true",
    "odinId" to "7379830069927298066",
    "os" to "mac",
    "priority_region" to "",
    "referer" to "",
    "user_is_login" to "false",
    "webcast_language" to "en"
)
