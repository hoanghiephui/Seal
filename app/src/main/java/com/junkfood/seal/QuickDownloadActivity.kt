package com.junkfood.seal

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.junkfood.seal.model.MainActivityUiState
import com.junkfood.seal.ui.common.LocalDarkTheme
import com.junkfood.seal.ui.common.LocalDynamicColorSwitch
import com.junkfood.seal.ui.common.LocalWindowWidthState
import com.junkfood.seal.ui.common.SettingsProvider
import com.junkfood.seal.ui.page.download.DownloadSettingDialog
import com.junkfood.seal.ui.page.download.DownloadViewModel
import com.junkfood.seal.ui.theme.SealTheme
import com.junkfood.seal.util.CONFIGURE
import com.junkfood.seal.util.CUSTOM_COMMAND
import com.junkfood.seal.util.PreferenceUtil
import com.junkfood.seal.util.PreferenceUtil.getBoolean
import com.junkfood.seal.util.ToastUtil
import com.junkfood.seal.util.matchUrlFromSharedText
import com.junkfood.seal.util.setLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "ShareActivity"

@AndroidEntryPoint
class QuickDownloadActivity : ComponentActivity() {
    private var url: String = ""
    private val viewModel: DownloadViewModel by viewModels()
    private fun handleShareIntent(intent: Intent) {
        Log.d(TAG, "handleShareIntent: $intent")
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.dataString?.let {
                    url = it
                }
            }

            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
                    ?.let { sharedContent ->
                        intent.removeExtra(Intent.EXTRA_TEXT)
                        matchUrlFromSharedText(sharedContent)
                            .let { matchedUrl ->
                                url = matchedUrl
                            }
                    }
            }
        }
    }

    private fun onDownloadStarted(customCommand: Boolean) {
        if (customCommand)
            Downloader.executeCommandWithUrl(url)
        else
            Downloader.quickDownload(url = url)
    }

    @OptIn(
        ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalMaterial3Api::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }

        window.run {
            setBackgroundDrawable(ColorDrawable(0))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            } else {
                setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
            }
        }
        handleShareIntent(intent)
        runBlocking {
            if (Build.VERSION.SDK_INT < 33) {
                setLanguage(PreferenceUtil.getLocaleFromPreference())
            }
        }
        val isDialogEnabled = CONFIGURE.getBoolean()

        if (url.isEmpty()) {
            finish()
        }

        if (!isDialogEnabled) {
            onDownloadStarted(CUSTOM_COMMAND.getBoolean())
            this.finish()
        }

        setContent {
            val scope = rememberCoroutineScope()
            SettingsProvider(
                windowWidthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            ) {
                SealTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                    isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                    isDynamicColorEnabled = LocalDynamicColorSwitch.current,
                ) {
                    var lastDownloadCount  by rememberSaveable { mutableIntStateOf(0) }
                    var isPlusMode  by rememberSaveable { mutableStateOf(false) }
                    val userState by viewModel.uiState.collectAsStateWithLifecycle()
                    LaunchedEffect(key1 = userState) {
                        if (userState is MainActivityUiState.Success) {
                            viewModel.resetPointsIfDaily((userState as MainActivityUiState.Success).userData.lastDay)
                            lastDownloadCount = (userState as MainActivityUiState.Success).userData.downloadCount
                            viewModel.currentPoints = lastDownloadCount
                            isPlusMode = (userState as MainActivityUiState.Success).userData.makePro
                        }
                    }
                    var showDialog by remember { mutableStateOf(true) }
                    val sheetState =
                        rememberModalBottomSheetState(skipPartiallyExpanded = true)

                    val useDialog = LocalWindowWidthState.current != WindowWidthSizeClass.Compact
                    DownloadSettingDialog(
                        useDialog = useDialog,
                        showDialog = showDialog,
                        isQuickDownload = true,
                        sheetState = sheetState,
                        onDownloadConfirm = {
                            ToastUtil.makeToast(R.string.service_title)
                            onDownloadStarted(PreferenceUtil.getValue(CUSTOM_COMMAND))
                        },
                        onDismissRequest = {
                            if (!useDialog) {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    showDialog = false
                                }
                            } else {
                                showDialog = false
                            }
                            this@QuickDownloadActivity.finish()
                        },
                        lastDownloadCount = lastDownloadCount,
                        isPlusMode = isPlusMode,
                        onMakePlus = {
                            startActivityWithData(this, WITH_SUB)
                            this@QuickDownloadActivity.finish()
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.let { handleShareIntent(it) }
        super.onNewIntent(intent)
    }

    private fun startActivityWithData(context: Context, data: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(KEY_MAKE, data)
            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
    companion object {
        const val KEY_MAKE = "KEY_MAKE"
        const val WITH_SUB = "WITH_SUB"
    }
}
