package com.junkfood.seal.ui.page.download

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.play.core.ktx.AppUpdateResult
import com.junkfood.seal.App
import com.junkfood.seal.BuildConfig
import com.junkfood.seal.Downloader
import com.junkfood.seal.R
import com.junkfood.seal.model.MainActivityUiState
import com.junkfood.seal.model.SupportModel
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.common.HapticFeedback.longPressHapticFeedback
import com.junkfood.seal.ui.common.HapticFeedback.slightHapticFeedback
import com.junkfood.seal.ui.common.LocalWindowWidthState
import com.junkfood.seal.ui.common.intState
import com.junkfood.seal.ui.component.AdViewState
import com.junkfood.seal.ui.component.ClearButton
import com.junkfood.seal.ui.component.FilledButtonWithIcon
import com.junkfood.seal.ui.component.HeaderUpdate
import com.junkfood.seal.ui.component.HelpDialog
import com.junkfood.seal.ui.component.NavigationBarSpacer
import com.junkfood.seal.ui.component.OutlinedButtonWithIcon
import com.junkfood.seal.ui.component.VideoCard
import com.junkfood.seal.ui.theme.PreviewThemeLight
import com.junkfood.seal.ui.theme.SealTheme
import com.junkfood.seal.util.CELLULAR_DOWNLOAD
import com.junkfood.seal.util.CONFIGURE
import com.junkfood.seal.util.CUSTOM_COMMAND
import com.junkfood.seal.util.DEBUG
import com.junkfood.seal.util.DISABLE_PREVIEW
import com.junkfood.seal.util.FileUtil
import com.junkfood.seal.util.NOTIFICATION
import com.junkfood.seal.util.PreferenceUtil
import com.junkfood.seal.util.PreferenceUtil.getBoolean
import com.junkfood.seal.util.PreferenceUtil.updateBoolean
import com.junkfood.seal.util.PreferenceUtil.updateInt
import com.junkfood.seal.util.SHOW_REVIEW
import com.junkfood.seal.util.ToastUtil
import com.junkfood.seal.util.isTikTokLink
import com.junkfood.seal.util.isYouTubeLink
import com.junkfood.seal.util.matchUrlFromClipboard
import com.junkfood.seal.util.openAppSettings
import com.junkfood.seal.util.permissionWriteStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import se.warting.inappupdate.compose.rememberInAppUpdateState


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun DownloadPage(
    navigateToSettings: () -> Unit = {},
    navigateToDownloads: () -> Unit = {},
    navigateToPlaylistPage: () -> Unit = {},
    navigateToFormatPage: () -> Unit = {},
    onNavigateToTaskList: () -> Unit = {},
    onNavigateToCookieGeneratorPage: (String) -> Unit = {},
    onNavigateToSupportedSite: () -> Unit = {},
    onViewAds: () -> Unit,
    onMakePlus: () -> Unit,
    downloadViewModel: DownloadViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val downloaderState by Downloader.downloaderState.collectAsStateWithLifecycle()
    val taskState by Downloader.taskState.collectAsStateWithLifecycle()
    val viewState by downloadViewModel.viewStateFlow.collectAsStateWithLifecycle()
    val playlistInfo by Downloader.playlistResult.collectAsStateWithLifecycle()
    val videoInfo by downloadViewModel.videoInfoFlow.collectAsStateWithLifecycle()
    val errorState by Downloader.errorState.collectAsStateWithLifecycle()
    val processCount by Downloader.processCount.collectAsStateWithLifecycle()
    val userState by downloadViewModel.uiState.collectAsStateWithLifecycle()
    var lastDownloadCount  by rememberSaveable { mutableIntStateOf(0) }
    var isPlusMode  by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = userState) {
        if (userState is MainActivityUiState.Success) {
            downloadViewModel.resetPointsIfDaily((userState as MainActivityUiState.Success).userData.lastDay)
            lastDownloadCount = (userState as MainActivityUiState.Success).userData.downloadCount
            downloadViewModel.currentPoints = lastDownloadCount
            isPlusMode = (userState as MainActivityUiState.Success).userData.makePro
        }
    }
    val nativeAd by downloadViewModel.adState.collectAsStateWithLifecycle()
    var showNotificationDialog by remember { mutableStateOf(false) }
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS) { isGranted: Boolean ->
            showNotificationDialog = false
            if (!isGranted) {
                ToastUtil.makeToast(R.string.permission_denied)
            }
        }
    } else null
    LaunchedEffect(key1 = BuildConfig.HOME_NATIVE) {
        downloadViewModel.loadAds(context, BuildConfig.HOME_NATIVE)
    }
    val updateState = rememberInAppUpdateState()
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val useDialog = LocalWindowWidthState.current != WindowWidthSizeClass.Compact
    val view = LocalView.current
    var showDownloadDialog by rememberSaveable { mutableStateOf(false) }
    var showMeteredNetworkDialog by remember { mutableStateOf(false) }
    var showAds by remember { mutableStateOf(true) }
    var isStartDownload by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }
    var showDownloadCompleteDialog by rememberSaveable { mutableStateOf(false) }
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = downloaderState, key2 = isStartDownload) {
        showAds = if (isStartDownload) {
            downloaderState !is Downloader.State.DownloadingVideo
        } else {
            true
        }
    }
    val showReview by SHOW_REVIEW.intState
    val checkNetworkOrDownload = {
        if (!PreferenceUtil.isNetworkAvailableForDownload()) {
            showMeteredNetworkDialog = true
        } else {
            isStartDownload = true
            isDownloaded = false
            downloadViewModel.startDownloadVideo()
        }
    }
    var permissionRequested: Boolean by rememberSaveable { mutableStateOf(false) }
    val storagePermission = rememberMultiplePermissionsState(
        permissions = permissionWriteStore
    )
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { wasGranted ->
        if (wasGranted.all { it.value }) {
            checkNetworkOrDownload()
        }
    }

    val checkPermissionOrDownload = {
        if (storagePermission.allPermissionsGranted ) {
            checkNetworkOrDownload()
        } else {
            if (storagePermission.shouldShowRationale) {
                permissionRequested = true
            } else {
                launcher.launch(permissionWriteStore.toTypedArray())
            }
        }
    }
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (permissionRequested) {
        HelpDialog(
            title = stringResource(id = R.string.permission_denied),
            text = stringResource(id = R.string.permission_write_setting),
            onDismissRequest = { permissionRequested = false }, dismissButton = null
        ) {
            TextButton(onClick = {
                permissionRequested = false
                context.openAppSettings()
            }) {
                Text(text = stringResource(id = R.string.go_to_setting))
            }
        }
    }

    val downloadCallback: () -> Unit = {
        view.slightHapticFeedback()
        keyboardController?.hide()
        if (viewState.url.isYouTubeLink()) {
            downloadViewModel.onNotSupportError(viewState.url)
        } else {
            if (NOTIFICATION.getBoolean() && notificationPermission?.status?.isGranted == false) {
                showNotificationDialog = true
            }
            if (CONFIGURE.getBoolean()) {
                showDownloadDialog = true

            } else {
                checkPermissionOrDownload()
            }
        }
    }

    if (showNotificationDialog) {
        NotificationPermissionDialog(onDismissRequest = {
            showNotificationDialog = false
            NOTIFICATION.updateBoolean(false)
        }, onPermissionGranted = {
            notificationPermission?.launchPermissionRequest()
        })
    }

    if (showMeteredNetworkDialog) {
        MeteredNetworkDialog(
            onDismissRequest = { showMeteredNetworkDialog = false },
            onAllowOnceConfirm = {
                downloadViewModel.startDownloadVideo()
                showMeteredNetworkDialog = false
            },
            onAllowAlwaysConfirm = {
                downloadViewModel.startDownloadVideo()
                CELLULAR_DOWNLOAD.updateBoolean(true)
                showMeteredNetworkDialog = false
            })
    }
    LaunchedEffect(key1 = updateState) {
        if (updateState.appUpdateResult is AppUpdateResult.InProgress
            || updateState.appUpdateResult is AppUpdateResult.Available
            || updateState.appUpdateResult is AppUpdateResult.Downloaded
        ) {
            showUpdateDialog = true
            delay(50)
            sheetState.show()
        }
        if (BuildConfig.DEBUG) {
            ToastUtil.makeToast("Update Status: ${updateState.appUpdateResult}")
        }
    }

    DisposableEffect(viewState.showPlaylistSelectionDialog) {
        if (!playlistInfo.entries.isNullOrEmpty() && viewState.showPlaylistSelectionDialog) navigateToPlaylistPage()
        onDispose { downloadViewModel.hidePlaylistDialog() }
    }

    DisposableEffect(viewState.showFormatSelectionPage) {
        if (viewState.showFormatSelectionPage) {
            if (!videoInfo.formats.isNullOrEmpty()) navigateToFormatPage()
        }
        onDispose { downloadViewModel.hideFormatPage() }
    }
    var showOutput by remember {
        mutableStateOf(DEBUG.getBoolean())
    }
    LaunchedEffect(downloaderState) {
        showOutput = PreferenceUtil.getValue(DEBUG) && downloaderState !is Downloader.State.Idle
        isStartDownload = downloaderState is Downloader.State.DownloadingVideo
        isDownloaded = downloaderState is Downloader.State.Downloaded
    }
    if (viewState.isUrlSharingTriggered) {
        downloadViewModel.onShareIntentConsumed()
        downloadCallback()
    }

    val showVideoCard by remember(downloaderState) {
        mutableStateOf(
            !PreferenceUtil.getValue(DISABLE_PREVIEW)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        DownloadPageImpl(downloaderState = downloaderState,
            taskState = taskState,
            viewState = viewState,
            errorState = errorState,
            downloadCallback = downloadCallback,
            navigateToSettings = navigateToSettings,
            navigateToDownloads = navigateToDownloads,
            onNavigateToTaskList = onNavigateToTaskList,
            processCount = processCount,
            showVideoCard = showVideoCard,
            showOutput = showOutput,
            showDownloadProgress = taskState.taskId.isNotEmpty(),
            showAdsCard = showAds,
            pasteCallback = {
                isStartDownload = false
                isDownloaded = false
                showAds = true
                matchUrlFromClipboard(
                    string = clipboardManager.getText().toString(),
                    isMatchingMultiLink = CUSTOM_COMMAND.getBoolean()
                )
                    .let { downloadViewModel.updateUrl(it) }
            },
            cancelCallback = {
                isStartDownload = false
                isDownloaded = false
                Downloader.cancelDownload()
            },
            onVideoCardClicked = { Downloader.openDownloadResult() },
            onUrlChanged = { url ->
                Downloader.clearErrorState()
                downloadViewModel.updateUrl(url)
            },
            nativeAd = nativeAd,
            isStartDownload = isStartDownload
        ) {
            SiteSupport(downloadViewModel.itemsSupport) {
                onNavigateToSupportedSite.invoke()
            }
        }


        DownloadSettingDialog(
            useDialog = useDialog,
            showDialog = showDownloadDialog,
            //sheetState = sheetState,
            onNavigateToCookieGeneratorPage = onNavigateToCookieGeneratorPage,
            onDownloadConfirm = { checkPermissionOrDownload() },
            onDismissRequest = {
                if (!useDialog) {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showDownloadDialog = false
                    }
                } else {
                    showDownloadDialog = false
                }
            },
            isTiktok = viewState.url.isTikTokLink(),
            lastDownloadCount = lastDownloadCount,
            isPlusMode = isPlusMode,
            onViewAds = onViewAds,
            onMakePlus = onMakePlus
        )
        if (isDownloaded) scope.launch {
            showDownloadCompleteDialog = true
            delay(50)
            sheetState.show()
        }
        val shareTitle = stringResource(id = R.string.share)
        DownloadCompleteDialog(
            showDialog = showDownloadCompleteDialog,
            sheetState = sheetState,
            onShare = {
                FileUtil.createIntentForSharingFile("${Downloader.filePathDownloaded}")
                    ?.runCatching {
                        context.startActivity(
                            Intent.createChooser(this, shareTitle)
                        )
                    }
            },
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showDownloadCompleteDialog = false
                    isDownloaded = false
                    Downloader.updateState(Downloader.State.Idle)
                    downloadViewModel.deductPoints(points = 1, currentPoints = lastDownloadCount)
                    SHOW_REVIEW.updateInt(showReview + 1)
                }
            },
            taskState = taskState,
            onVideoCardClicked = {
                Downloader.openDownloadResult()
            }
        )

        HeaderUpdate(
            showDialog = showUpdateDialog,
            sheetState = sheetState,
            updateState = updateState,
            context = context,
            rememberCoroutineScope = scope,
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showUpdateDialog = false
                }
            }
        )
    }

}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun DownloadPageImpl(
    downloaderState: Downloader.State,
    taskState: Downloader.DownloadTaskItem,
    viewState: DownloadViewModel.ViewState,
    errorState: Downloader.ErrorState,
    showVideoCard: Boolean = false,
    showAdsCard: Boolean = true,
    showOutput: Boolean = false,
    showDownloadProgress: Boolean = false,
    processCount: Int = 0,
    downloadCallback: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToDownloads: () -> Unit = {},
    onNavigateToTaskList: () -> Unit = {},
    pasteCallback: () -> Unit = {},
    cancelCallback: () -> Unit = {},
    onVideoCardClicked: () -> Unit = {},
    onUrlChanged: (String) -> Unit = {},
    isPreview: Boolean = false,
    nativeAd: AdViewState,
    isStartDownload: Boolean,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val clipboardManager = LocalClipboardManager.current

    val showCancelButton =
        downloaderState is Downloader.State.DownloadingPlaylist || downloaderState is Downloader.State.DownloadingVideo
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {}, modifier = Modifier.padding(horizontal = 8.dp), navigationIcon = {
            TooltipBox(
                state = rememberTooltipState(),
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = stringResource(id = R.string.settings))
                    }
                }) {
                IconButton(
                    onClick = {
                        view.slightHapticFeedback()
                        navigateToSettings()
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(id = R.string.settings)
                    )
                }
            }

        }, actions = {
            /*BadgedBox(badge = {
                if (processCount > 0)
                    Badge(
                        modifier = Modifier.offset(
                            x = (-16).dp,
                            y = (8).dp
                        )
                    ) { Text("$processCount") }
            }) {
                TooltipBox(state = rememberTooltipState(),
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = stringResource(id = R.string.running_tasks))
                        }
                    }) {
                    IconButton(
                        onClick = {
                            view.slightHapticFeedback()
                            onNavigateToTaskList()
                        },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Terminal,
                            contentDescription = stringResource(id = R.string.running_tasks)
                        )
                    }
                }
            }*/
            TooltipBox(state = rememberTooltipState(),
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = stringResource(id = R.string.downloads_history))
                    }
                }) {
                IconButton(
                    onClick = {
                        view.slightHapticFeedback()
                        navigateToDownloads()
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Subscriptions,
                        contentDescription = stringResource(id = R.string.downloads_history)
                    )
                }
            }
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TitleWithProgressIndicator(
                isDownloadingPlaylist = downloaderState is Downloader.State.DownloadingPlaylist,
                showDownloadText = showCancelButton,
                currentIndex = downloaderState.run { if (this is Downloader.State.DownloadingPlaylist) currentItem else 0 },
                downloadItemCount = downloaderState.run { if (this is Downloader.State.DownloadingPlaylist) itemCount else 0 },
            )


            Column(
                Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp)
            ) {
                with(taskState) {
                    AnimatedVisibility(
                        visible = showDownloadProgress && showVideoCard || showAdsCard
                    ) {
                        VideoCard(
                            modifier = Modifier,
                            title = title,
                            author = uploader,
                            thumbnailUrl = thumbnailUrl,
                            progress = progress,
                            showCancelButton = downloaderState is Downloader.State.DownloadingPlaylist || downloaderState is Downloader.State.DownloadingVideo,
                            onCancel = cancelCallback,
                            fileSizeApprox = fileSizeApprox,
                            duration = duration,
                            onClick = onVideoCardClicked,
                            isPreview = isPreview,
                            isAds = showAdsCard,
                            nativeAd = nativeAd
                        )
                    }
                    InputUrl(
                        url = viewState.url,
                        progress = progress,
                        showDownloadProgress = showDownloadProgress && !showVideoCard,
                        error = errorState != Downloader.ErrorState.None,
                        showCancelButton = showCancelButton && !showVideoCard,
                        onCancel = cancelCallback,
                        onDone = downloadCallback,
                        showProgressIndicator = downloaderState is Downloader.State.FetchingInfo
                    ) { url -> onUrlChanged(url) }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                        visible = progressText.isNotEmpty() && showOutput
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 12.dp),
                            text = progressText,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButtonWithIcon(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            onClick = pasteCallback,
                            icon = Icons.Outlined.ContentPaste,
                            text = stringResource(R.string.paste)
                        )

                        FilledButtonWithIcon(
                            onClick = downloadCallback,
                            icon = Icons.Outlined.FileDownload,
                            text = stringResource(R.string.download),
                            enabled = viewState.url.isNotBlank() && !isStartDownload
                        )
                    }

                }
                AnimatedVisibility(visible = errorState != Downloader.ErrorState.None) {
                    ErrorMessage(
                        title = errorState.title,
                        errorReport = errorState.report,
                        showButton = errorState != Downloader.ErrorState.VerifyError(
                            viewState.url,
                            stringResource(id = R.string.paste_youtube_fail_msg)
                        )
                    ) {
                        view.longPressHapticFeedback()
                        clipboardManager.setText(AnnotatedString(App.getVersionReport() + "\nURL: ${errorState.url}\n${errorState.report}"))
                        ToastUtil.makeToast(R.string.error_copied)
                    }
                }
                content()
//                val output = Downloader.mutableProcessOutput
//                LazyRow() {
//                    items(output.toList()) { entry ->
//                        TextField(
//                            value = entry.second,
//                            label = { Text(entry.first) },
//                            onValueChange = {},
//                            readOnly = true,
//                            minLines = 10,
//                            maxLines = 10,
//                        )
//                    }
//                }
//                    PreviewFormat()
                NavigationBarSpacer()
                Spacer(modifier = Modifier.height(160.dp))
            }
        }
    }
}

@Composable
fun InputUrl(
    url: String,
    error: Boolean,
    showDownloadProgress: Boolean = false,
    showProgressIndicator: Boolean = false,
    progress: Float,
    onDone: () -> Unit,
    showCancelButton: Boolean,
    onCancel: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        OutlinedTextField(
            enabled = !showProgressIndicator,
            value = url,
            isError = error,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.video_url)) },
            modifier = Modifier
                .padding(0f.dp, 16f.dp)
                .fillMaxWidth(),

            textStyle = MaterialTheme.typography.bodyLarge,
            maxLines = 3,
            trailingIcon = {
                if (url.isNotEmpty()) ClearButton { onValueChange("") }
            }, keyboardActions = KeyboardActions(onDone = {

                onDone()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
        AnimatedVisibility(visible = showProgressIndicator) {
            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp), strokeWidth = 3.dp
                )
            }
        }
    }
    AnimatedVisibility(visible = showDownloadProgress) {
        Row(
            Modifier.padding(0.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val progressAnimationValue by animateFloatAsState(
                targetValue = progress / 100f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "progress"
            )
            if (progressAnimationValue < 0) LinearProgressIndicator(
                modifier = Modifier
                    .weight(0.75f)
                    .clip(MaterialTheme.shapes.large),
            )
            else LinearProgressIndicator(
                progress = { progressAnimationValue },
                modifier = Modifier
                    .weight(0.75f)
                    .clip(MaterialTheme.shapes.large),
            )
            Text(
                text = if (progress < 0) "0%" else "$progress%",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.25f)
            )
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = showCancelButton) {
            OutlinedButtonWithIcon(
                onClick = onCancel,
                icon = Icons.Outlined.Cancel,
                text = stringResource(id = R.string.cancel),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

}

@Composable
fun TitleWithProgressIndicator(
    showDownloadText: Boolean = true,
    isDownloadingPlaylist: Boolean = true,
    currentIndex: Int = 1,
    downloadItemCount: Int = 4,
) {
    Column(modifier = Modifier.padding(start = 12.dp, top = 0.dp)) {
        AnimatedVisibility(visible = showDownloadText) {
            Text(
                if (isDownloadingPlaylist) stringResource(R.string.playlist_indicator_text).format(
                    currentIndex,
                    downloadItemCount
                )
                else stringResource(R.string.downloading_indicator_text),
                modifier = Modifier.padding(start = 12.dp, top = 3.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    title: String,
    errorReport: String,
    showButton: Boolean,
    onButtonClicked: () -> Unit = {}
) {
    val view = LocalView.current
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier,
                        text = title,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            var isExpanded by remember { mutableStateOf(false) }

            Text(
                text = errorReport,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isExpanded) Int.MAX_VALUE else 8,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable(
                        enabled = !isExpanded, onClickLabel = stringResource(
                            id = R.string.expand
                        ), onClick = {
                            view.slightHapticFeedback()
                            isExpanded = true
                        }
                    )
                    .padding(4.dp),
                onTextLayout = {
                    isExpanded = !it.hasVisualOverflow
                }
            )
            if (showButton) {
                Spacer(modifier = Modifier.height(8.dp))


                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = onButtonClicked,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    ) {
                        Text(text = stringResource(id = R.string.copy_error_report))
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun ErrorPreview() {
    SealTheme {
        Surface {
            LazyColumn {
                item {
                    ErrorMessage(
                        title = stringResource(id = R.string.download_error_msg),
                        errorReport = ERROR_REPORT_SAMPLE,
                        showButton = true
                    ) {}
                }
            }

        }
    }
}


@Composable
fun FABs(
    modifier: Modifier = Modifier,
    downloadCallback: () -> Unit = {},
    pasteCallback: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(6.dp), horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = pasteCallback,
            content = {
                Icon(
                    Icons.Outlined.ContentPaste, contentDescription = stringResource(R.string.paste)
                )
            },
            modifier = Modifier.padding(vertical = 12.dp),
        )
        FloatingActionButton(
            onClick = downloadCallback,
            content = {
                Icon(
                    Icons.Outlined.FileDownload,
                    contentDescription = stringResource(R.string.download)
                )
            },
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }

}

@Composable
@Preview
fun DownloadPagePreview() {
    PreviewThemeLight {
        Column {
            DownloadPageImpl(
                downloaderState = Downloader.State.DownloadingVideo,
                taskState = Downloader.DownloadTaskItem(),
                viewState = DownloadViewModel.ViewState(),
                errorState = Downloader.ErrorState.DownloadError(
                    url = "",
                    report = ERROR_REPORT_SAMPLE
                ),
                processCount = 99,
                isPreview = true,
                showDownloadProgress = true,
                showVideoCard = false,
                nativeAd = AdViewState.Default,
                isStartDownload = false
            ) {}
        }
    }
}

@Composable
private fun SiteSupport(
    items: List<SupportModel>,
    navigateToSupportedSite: () -> Unit
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Column {
            Text(
                text = stringResource(R.string.feature_foryou_onboarding_guidance_title),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.feature_foryou_onboarding_guidance_subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 24.dp, end = 24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .height(120.dp)
                    .testTag("forYou:feed"),
            ) {
                onboarding(items)
            }
            Button(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxWidth(), onClick = navigateToSupportedSite) {
                Text(text = "View all")
            }
        }
    }
}

private fun LazyGridScope.onboarding(
    items: List<SupportModel>
) {
    items(items) {
        ElevatedCard(
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(containerColor = Color(it.color)),
            modifier = Modifier
                .wrapContentWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImageImpl(
                    modifier = Modifier
                        .padding()
                        .size(40.dp)
                        .aspectRatio(4f / 4f, matchHeightConstraintsFirst = true)
                        .clip(MaterialTheme.shapes.small),
                    model = it.urlIcon,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    isPreview = false
                )
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    text = it.name,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

        }
    }
}

private const val ERROR_REPORT_SAMPLE =
    """[sample] Extracting URL: https://www.example.com
[sample] sample: Downloading webpage
[sample] sample: Downloading android player API JSON
[info] Available automatic captions for sample:
[info] Available automatic captions for sample:
[sample] sample: Downloading android player API JSON
[info] Available automatic captions for sample:
[info] Available automatic captions for sample:"""
