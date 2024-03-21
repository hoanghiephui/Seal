package com.junkfood.seal.ui.page.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.junkfood.seal.Downloader
import com.junkfood.seal.R
import com.junkfood.seal.ui.component.FilledButtonWithIcon
import com.junkfood.seal.ui.component.OutlinedButtonWithIcon
import com.junkfood.seal.ui.component.SealModalBottomSheet
import com.junkfood.seal.ui.component.VideoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadCompleteDialog(
    showDialog: Boolean = false,
    sheetState: SheetState,
    taskState: Downloader.DownloadTaskItem,
    onShare: () -> Unit,
    onDismissRequest: () -> Unit,
    onVideoCardClicked: () -> Unit = {}
) {
    val sheetContent: @Composable () -> Unit = {
        with(taskState) {
            VideoCard(
                modifier = Modifier,
                title = title,
                author = uploader,
                thumbnailUrl = thumbnailUrl,
                progress = progress,
                showCancelButton = false,
                onCancel = {},
                fileSizeApprox = fileSizeApprox,
                duration = duration,
                onClick = onVideoCardClicked,
                isPreview = false,
                isAds = false
            )
        }
    }
    if (showDialog) {
        SealModalBottomSheet(
            sheetState = sheetState,
            horizontalPadding = PaddingValues(horizontal = 20.dp),
            onDismissRequest = onDismissRequest,
            content = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Icon(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        imageVector = Icons.Outlined.CloudDownload,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.download_complete),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    sheetContent()
                    val state = rememberLazyListState()
                    LaunchedEffect(sheetState.isVisible) {
                        state.scrollToItem(0)
                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.End,
                        state = state,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            OutlinedButtonWithIcon(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                onClick = {
                                    onVideoCardClicked.invoke()
                                    onDismissRequest.invoke()
                                },
                                icon = Icons.Outlined.PlayArrow,
                                text = stringResource(R.string.player)
                            )
                        }
                        item {
                            FilledButtonWithIcon(
                                onClick = {
                                    onShare.invoke()
                                    onDismissRequest.invoke()
                                },
                                icon = Icons.Outlined.Share,
                                text = stringResource(R.string.share),
                            )
                        }
                    }
                }
            })
    }
}
