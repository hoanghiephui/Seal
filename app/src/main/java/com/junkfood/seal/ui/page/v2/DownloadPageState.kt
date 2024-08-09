package com.junkfood.seal.ui.page.v2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.junkfood.seal.model.ParcelableResult
import com.junkfood.seal.ui.common.HapticFeedback.slightHapticFeedback
import com.junkfood.seal.ui.common.Route
import com.junkfood.seal.ui.page.download.DownloadViewModel
import com.junkfood.seal.ui.page.v2.section.DownloadContentSectionState
import com.junkfood.seal.ui.page.v2.section.rememberDownloadInitialSectionState
import com.junkfood.seal.ui.page.v2.section.rememberDownloadLoadedSectionState
import com.junkfood.seal.ui.page.v2.section.rememberDownloadLoadingSectionState

data class DownloadPageState(
    val contentSectionState: DownloadContentSectionState,
    val onNavigateToCookieGeneratorPage: (String) -> Unit = {},
    val onViewAds: () -> Unit,
    val navigateToSettings: () -> Unit,
    val navigateToDownloads: () -> Unit,
    val screenHeight: Int,
    val screenWidth: Int
)

@Composable
fun rememberDownloadPageState(
    downloadViewModel: DownloadViewModel,
    navigator: NavHostController,
    onNavToCookieGeneratorPage: (String) -> Unit = {},
    onViewAds: () -> Unit,
    screenHeight: Int,
    screenWidth: Int,
): DownloadPageState {

    val view = LocalView.current
    val viewState by downloadViewModel.viewStateFlow.collectAsStateWithLifecycle()
    val videoInfo by downloadViewModel.videoInfoFlow.collectAsStateWithLifecycle()
    val userState by downloadViewModel.uiState.collectAsStateWithLifecycle()
    val nativeAd by downloadViewModel.adState.collectAsStateWithLifecycle()

    val explorerTiktok = downloadViewModel.exploreVideos
    val isInitialLoading = downloadViewModel.isInitialLoading
    val isRefreshLoading = downloadViewModel.isRefreshLoading

    val navigateToSettings = remember {
        {
            view.slightHapticFeedback()
            navigator.navigate(Route.SETTINGS) {
                launchSingleTop = true
            }
        }
    }
    val navigateToDownloads = remember {
        {
            view.slightHapticFeedback()
            navigator.navigate(Route.DOWNLOADS)
        }
    }


    val contentSectionState = when {
        isInitialLoading -> rememberDownloadLoadingSectionState()
        explorerTiktok is ParcelableResult.Success ->
            rememberDownloadLoadedSectionState(
                onNavigateToCookieGeneratorPage = onNavToCookieGeneratorPage,
                onViewAds = onViewAds,
                navigation = navigator,
                viewState = viewState,
                videoInfo = videoInfo,
                userState = userState,
                nativeAd = nativeAd,
                explorerTiktok = explorerTiktok.result
            )

        else -> rememberDownloadInitialSectionState(
            isRefreshLoading = isRefreshLoading,
            exploreResponse = explorerTiktok,
            initialLoadIfNeeded = {
                downloadViewModel.initialLoadIfNeeded(
                    screenHeight = screenHeight,
                    screenWidth = screenWidth
                )
            },
            refresh = downloadViewModel::refreshLoad
        )
    }

    return remember(
        contentSectionState,
        view
    ) {
        DownloadPageState(
            contentSectionState = contentSectionState,
            onNavigateToCookieGeneratorPage = onNavToCookieGeneratorPage,
            onViewAds = onViewAds,
            navigateToSettings = navigateToSettings,
            navigateToDownloads = navigateToDownloads,
            screenHeight = screenHeight,
            screenWidth = screenWidth
        )
    }
}
