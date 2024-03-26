package com.junkfood.seal.ui.component

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.junkfood.seal.SHOW_ADS

/**
 * Ad loader to load Max Native ads with Templates API using Jetpack Compose.
 */
class MaxTemplateNativeAdViewComposableLoader {
    var nativeAdView = mutableStateOf<AdViewState>(AdViewState.Default)
    private var nativeAd: MaxAd? = null
    private var nativeAdLoader: MaxNativeAdLoader? = null
    fun destroy() {
        // Must destroy native ad or else there will be memory leaks.
        if (nativeAd != null) {
            // Call destroy on the native ad from any native ad loader.
            nativeAdLoader?.destroy(nativeAd)
        }

        // Destroy the actual loader itself
        nativeAdLoader?.destroy()
    }

    fun loadAd(
        context: Context,
        adUnitIdentifier: String,
    ) {
        nativeAdLoader = MaxNativeAdLoader(adUnitIdentifier, context)

        val adListener = object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(loadedNativeAdView: MaxNativeAdView?, ad: MaxAd) {
                // Cleanup any pre-existing native ad to prevent memory leaks.
                if (nativeAd != null) {
                    nativeAdLoader?.destroy(nativeAd)
                    nativeAdView.value.let {
                        if (it is AdViewState.LoadAd) {
                            it.adView.removeAllViews()
                            it.adView.addView(loadedNativeAdView)
                        }
                    }
                }

                nativeAd = ad // Save ad for cleanup.
                loadedNativeAdView?.let {
                    nativeAdView.value = AdViewState.LoadAd(loadedNativeAdView)
                }
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                nativeAdView.value = AdViewState.LoadFail
                Log.e("Applovin", error.message)
            }

            override fun onNativeAdClicked(ad: MaxAd) {
            }

            override fun onNativeAdExpired(nativeAd: MaxAd) {
            }
        }
        nativeAdLoader?.apply {
            setNativeAdListener(adListener)
            loadAd()
        }
    }
}

/**
 * Jetpack Compose function to display MAX native ads using the Templates API.
 */
@Composable
fun MaxTemplateNativeAdViewComposable(
    adViewState: AdViewState,
    adType: AdType = AdType.MEDIUM
) {
    if (!SHOW_ADS) return
    Crossfade(adViewState, label = "MaxTemplateNativeAdView") { viewState ->
        when (viewState) {
            is AdViewState.LoadFail -> Unit
            is AdViewState.Default -> {
                if (adType == AdType.SMALL) {
                    Box(
                        modifier = Modifier.padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Loading()
                    }
                } else {
                    AdsView()
                }
            }

            is AdViewState.LoadAd -> {
                AndroidView(
                    factory = {
                        viewState.adView.apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            this.mainView.setBackgroundColor(
                                ContextCompat.getColor(
                                    it,
                                    android.R.color.transparent
                                )
                            )
                        }.also {
                            if (it.parent != null) (it.parent as ViewGroup).removeView(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adType.height)
                )
            }
        }
    }

}

val AdType.height get() = if (this == AdType.MEDIUM) 300.dp else 125.dp

sealed interface AdViewState {
    data class LoadAd(
        val adView: MaxNativeAdView
    ) : AdViewState

    data object Default : AdViewState
    data object LoadFail : AdViewState
}

enum class AdType {
    SMALL,
    MEDIUM
}
