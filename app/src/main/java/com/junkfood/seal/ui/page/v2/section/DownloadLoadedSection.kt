package com.junkfood.seal.ui.page.v2.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.android.video.model.ItemListItem
import com.junkfood.seal.ui.component.AdViewState
import com.junkfood.seal.ui.component.VideoCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@Composable
fun DownloadLoadedSection(
    sectionState: DownloadLoadedSectionState,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 12.dp,
            modifier = Modifier
                .testTag("forYou:feed"),
            state = sectionState.lazyListState,
        ) {
            exploreFeed(
                itemVideos = sectionState.explorerTiktok.itemList.toImmutableList()
            )
        }
    }
}

private fun LazyStaggeredGridScope.exploreFeed(
    itemVideos: ImmutableList<ItemListItem>
) {
    items(
        items = itemVideos,
        key = { it.id ?: "" },
        contentType = { "exploreFeedItem" },
    ) { exploreFeedItem ->
        VideoCard(
            modifier = Modifier,
            title = exploreFeedItem.desc ?: exploreFeedItem.contents?.first()?.desc ?: "",
            author = exploreFeedItem.author?.nickname ?: "",
            thumbnailUrl = exploreFeedItem.video?.cover ?: "",
            showCancelButton = false,
            onCancel = {},
            onClick = {},
            isPreview = false,
            isAds = false,
            nativeAd = AdViewState.Default,
            onMakePlus = {

            },
            progress = 0f,
            fileSizeApprox = 0.0,
            duration = 0
        )
    }
}
