package com.junkfood.seal.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefresh(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    val state = rememberPullRefreshState(isRefreshing, onRefresh)
    Box(modifier.pullRefresh(state)) {
        content()

        PullRefreshIndicator(isRefreshing, state, modifier.align(Alignment.TopCenter))
    }
}
