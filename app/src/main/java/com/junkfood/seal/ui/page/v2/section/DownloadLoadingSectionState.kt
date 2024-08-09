package com.junkfood.seal.ui.page.v2.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data object DownloadLoadingSectionState : DownloadContentSectionState

@Composable
fun rememberDownloadLoadingSectionState(): DownloadLoadingSectionState {
    return remember {
        DownloadLoadingSectionState
    }
}
