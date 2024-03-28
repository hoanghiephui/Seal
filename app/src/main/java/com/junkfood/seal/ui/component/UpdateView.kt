package com.junkfood.seal.ui.component

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.play.core.ktx.AppUpdateResult
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.motion.materialSharedAxisYIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import se.warting.inappupdate.compose.APP_UPDATE_REQUEST_CODE
import se.warting.inappupdate.compose.InAppUpdateState
import se.warting.inappupdate.compose.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderUpdate(
    updateState: InAppUpdateState,
    context: Context,
    showDialog: Boolean = false,
    sheetState: SheetState,
    rememberCoroutineScope: CoroutineScope,
    onDismissRequest: () -> Unit
) {
    val sheetContent: @Composable () -> Unit = {
        AnimatedContent(
            targetState = updateState.appUpdateResult,
            label = "",
            transitionSpec = {
                (materialSharedAxisYIn(initialOffsetX = { it / 4 })).togetherWith(
                    fadeOut(tween(durationMillis = 80))
                )
            }) { result ->
            when (result) {
                is AppUpdateResult.Available -> {
                    Column {
                        Text(
                            text = stringResource(id = R.string.update_content),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 24.dp)
                        )
                        Button(
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp, bottom = 10.dp)
                                .fillMaxWidth(),
                            onClick = {
                                rememberCoroutineScope.launch {
                                    result.startFlexibleUpdate(
                                        context.findActivity(), APP_UPDATE_REQUEST_CODE
                                    )
                                    onDismissRequest.invoke()
                                }
                            },
                        ) {
                            Text(stringResource(R.string.update_now))
                        }

                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                    }
                }

                is AppUpdateResult.InProgress -> {
                    Column {
                        val updateProgress: Long =
                            if (result.installState.totalBytesToDownload() == 0L) {
                                0L
                            } else {
                                (result.installState.bytesDownloaded() * 100L /
                                        result.installState.totalBytesToDownload())
                            }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            LoadingView()
                        }
                        Text(
                            text = stringResource(id = R.string.downloading),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 24.dp)
                        )
                        val process = updateProgress.toFloat() / 100f
                        LinearProgressIndicator(
                            progress = { process },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                        )
                        Text(
                            text = "${updateProgress}%",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }

                is AppUpdateResult.Downloaded -> {
                    Column {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = stringResource(id = R.string.update_done),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 24.dp)
                        )

                        Button(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            onClick = {
                                rememberCoroutineScope.launch {
                                    result.completeUpdate()
                                    onDismissRequest.invoke()
                                }
                            },
                        ) {
                            Text(stringResource(R.string.install_now))
                        }
                    }
                }

                else -> {
                    onDismissRequest.invoke()
                }
            }
        }
    }

    if (showDialog) {
        SealModalBottomSheet(
            sheetState = sheetState,
            horizontalPadding = PaddingValues(horizontal = 10.dp),
            onDismissRequest = onDismissRequest,
            content = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Update,
                            contentDescription = null
                        )
                        Text(
                            text = "Software update",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .padding(16.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }

                    sheetContent()
                }
            })
    }

}
