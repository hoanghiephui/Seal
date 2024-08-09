package com.junkfood.seal.ui.page.v2.section

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.junkfood.seal.model.ParcelableResult
import com.junkfood.seal.ui.common.PullRefresh
import com.junkfood.seal.ui.component.NetworkError

@Composable
internal fun DownloadInitialSection(
    sectionState: DownloadInitialSectionState,
    padding: PaddingValues
) {
    PullRefresh(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        isRefreshing = sectionState.isRefreshLoading,
        onRefresh = sectionState.refresh,
    ) {
        if (sectionState.exploreResponse is ParcelableResult.Failure) {
            NetworkError(
                modifier = Modifier.align(Alignment.Center),
                onClickReload = sectionState.initialLoadIfNeeded,
            )
        }
    }
}
