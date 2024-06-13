package com.junkfood.seal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.applovin.mediation.ads.MaxRewardedAd
import com.junkfood.seal.App.Companion.context
import com.junkfood.seal.QuickDownloadActivity.Companion.KEY_MAKE
import com.junkfood.seal.model.MainActivityUiState
import com.junkfood.seal.ui.ads.AdMaxRewardedLoader
import com.junkfood.seal.ui.ads.AdRewardedCallback
import com.junkfood.seal.ui.common.LocalDarkTheme
import com.junkfood.seal.ui.common.LocalDynamicColorSwitch
import com.junkfood.seal.ui.common.SettingsProvider
import com.junkfood.seal.ui.page.HomeEntry
import com.junkfood.seal.ui.page.billing.BillingPlusViewModel
import com.junkfood.seal.ui.page.download.DownloadViewModel
import com.junkfood.seal.ui.page.settings.network.CookiesViewModel
import com.junkfood.seal.ui.theme.SealTheme
import com.junkfood.seal.util.PreferenceUtil
import com.junkfood.seal.util.ToastUtil
import com.junkfood.seal.util.matchUrlFromSharedText
import com.junkfood.seal.util.setLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity(), AdRewardedCallback {
    private val downloadViewModel: DownloadViewModel by viewModels()
    private val billingViewModel by viewModels<BillingPlusViewModel>()
    private val adMaxRewardedLoader = AdMaxRewardedLoader(this)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                downloadViewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }
        runBlocking {
            if (Build.VERSION.SDK_INT < 33) {
                setLanguage(PreferenceUtil.getLocaleFromPreference())
            }
        }
        billingViewModel.onVerify(this)
        context = this.baseContext
        setContent {
            val cookiesViewModel: CookiesViewModel = viewModel()

            val isUrlSharingTriggered =
                downloadViewModel.viewStateFlow.collectAsState().value.isUrlSharingTriggered
            val windowSizeClass = calculateWindowSizeClass(this)
            SettingsProvider(windowWidthSizeClass = windowSizeClass.widthSizeClass) {
                SealTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                    isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                    isDynamicColorEnabled = LocalDynamicColorSwitch.current,
                ) {
                    HomeEntry(
                        downloadViewModel = downloadViewModel,
                        cookiesViewModel = cookiesViewModel,
                        isUrlShared = isUrlSharingTriggered,
                        onViewAds = {
                            adMaxRewardedLoader.createRewardedAd(this, BuildConfig.HOME_REWARDED)
                        }
                    )
                }
            }

            handleShareIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleShareIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleShareIntent(intent: Intent) {
        Log.d(TAG, "handleShareIntent: $intent")

        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.dataString?.let {
                    sharedUrl = it
                    downloadViewModel.updateUrl(sharedUrl, true)
                }
            }

            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
                    ?.let { sharedContent ->
                        intent.removeExtra(Intent.EXTRA_TEXT)
                        matchUrlFromSharedText(sharedContent)
                            .let { matchedUrl ->
                                if (sharedUrl != matchedUrl) {
                                    sharedUrl = matchedUrl
                                    downloadViewModel.updateUrl(sharedUrl, true)
                                }
                            }
                    }
            } else -> {
                val dataReceived = intent.getStringExtra(KEY_MAKE)
                downloadViewModel.makeUp(dataReceived)
            }
        }

    }

    companion object {
        private const val TAG = "MainActivity"
        private var sharedUrl = ""

    }

    override fun onLoaded(rewardedAd: MaxRewardedAd) {
        if (rewardedAd.isReady) {
            rewardedAd.showAd(this)
        }
    }

    override fun onAdRewardLoadFail() {
        ToastUtil.makeToast(R.string.add_point_fail)
    }

    override fun onUserRewarded(amount: Int) {
        downloadViewModel.addPoints(5, downloadViewModel.currentPoints)
        ToastUtil.makeToast(R.string.add_point_success)
    }

    override fun onShowFail() {
        ToastUtil.makeToast(R.string.add_point_fail)
    }
}





