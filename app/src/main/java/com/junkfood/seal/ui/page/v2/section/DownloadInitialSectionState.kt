package com.junkfood.seal.ui.page.v2.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.android.video.model.TiktokExploreResponse
import com.junkfood.seal.model.ParcelableResult

internal data class DownloadInitialSectionState(
    val isRefreshLoading: Boolean,
    val exploreResponse: ParcelableResult<TiktokExploreResponse>?,
    val initialLoadIfNeeded: () -> Unit,
    val refresh: () -> Unit
) : DownloadContentSectionState

@Composable
internal fun rememberDownloadInitialSectionState(
    isRefreshLoading: Boolean,
    exploreResponse: ParcelableResult<TiktokExploreResponse>?,
    initialLoadIfNeeded: () -> Unit,
    refresh: () -> Unit
): DownloadInitialSectionState {
    return remember(isRefreshLoading, exploreResponse, initialLoadIfNeeded, refresh) {
        DownloadInitialSectionState(
            isRefreshLoading = isRefreshLoading,
            exploreResponse = exploreResponse,
            initialLoadIfNeeded = initialLoadIfNeeded,
            refresh = refresh
        )
    }
}
