package com.junkfood.seal.ui.page.v2.section

import android.content.Context
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.android.video.model.TiktokExploreResponse
import com.junkfood.seal.model.MainActivityUiState
import com.junkfood.seal.ui.common.Route
import com.junkfood.seal.ui.component.AdViewState
import com.junkfood.seal.ui.page.download.DownloadViewModel
import com.junkfood.seal.util.VideoInfo

data class DownloadLoadedSectionState(
    val navigateToPlaylistPage: () -> Unit = {},
    val navigateToFormatPage: () -> Unit = {},
    val onNavigateToTaskList: () -> Unit = {},
    val onNavigateToCookieGeneratorPage: (String) -> Unit = {},
    val onNavigateToSupportedSite: () -> Unit = {},
    val onViewAds: () -> Unit,
    val onMakePlus: () -> Unit,
    val context: Context,
    val lazyListState: LazyStaggeredGridState,
    val viewState: DownloadViewModel.ViewState,
    val videoInfo: VideoInfo,
    val userState: MainActivityUiState,
    val nativeAd: AdViewState,
    val explorerTiktok: TiktokExploreResponse
) : DownloadContentSectionState

@Composable
fun rememberDownloadLoadedSectionState(
    onNavigateToCookieGeneratorPage: (String) -> Unit = {},
    onViewAds: () -> Unit,
    navigation: NavHostController,
    viewState: DownloadViewModel.ViewState,
    videoInfo: VideoInfo,
    userState: MainActivityUiState,
    nativeAd: AdViewState,
    explorerTiktok: TiktokExploreResponse
): DownloadLoadedSectionState {
    val context = LocalContext.current
    val lazyListState = rememberLazyStaggeredGridState()



    val navigateToPlaylistPage = remember {
        { navigation.navigate(Route.PLAYLIST) }
    }
    val navigateToFormatPage = remember {
        { navigation.navigate(Route.FORMAT_SELECTION) }
    }
    val onNavigateToTaskList = remember {
        { navigation.navigate(Route.TASK_LIST) }
    }
    val onNavigateToSupportedSite = remember {
        { navigation.navigate(Route.SUPPORTED_SITE_ROUTER) }
    }
    val onMakePlus = remember {
        { navigation.navigate(Route.DONATE) }
    }

    return remember(
        context,
        lazyListState,
        onNavigateToCookieGeneratorPage,
        onViewAds,
        viewState,
        videoInfo,
        userState,
        nativeAd,
        explorerTiktok
    ) {
        DownloadLoadedSectionState(
            navigateToPlaylistPage = navigateToPlaylistPage,
            navigateToFormatPage = navigateToFormatPage,
            onNavigateToTaskList = onNavigateToTaskList,
            onNavigateToCookieGeneratorPage,
            onNavigateToSupportedSite = onNavigateToSupportedSite,
            onViewAds,
            onMakePlus = onMakePlus,
            context,
            lazyListState = lazyListState,
            viewState = viewState,
            videoInfo = videoInfo,
            userState = userState,
            nativeAd = nativeAd,
            explorerTiktok = explorerTiktok
        )
    }
}
