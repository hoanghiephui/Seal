package com.junkfood.seal.ui.page.download

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.junkfood.seal.App.Companion.applicationScope
import com.junkfood.seal.App.Companion.context
import com.junkfood.seal.Downloader
import com.junkfood.seal.Downloader.State
import com.junkfood.seal.Downloader.manageDownloadError
import com.junkfood.seal.Downloader.notSupportError
import com.junkfood.seal.Downloader.updatePlaylistResult
import com.junkfood.seal.R
import com.junkfood.seal.SHOW_ADS
import com.junkfood.seal.model.MainActivityUiState
import com.junkfood.seal.model.SupportModel
import com.junkfood.seal.repository.OfflineFirstRepository
import com.junkfood.seal.ui.component.AdViewState
import com.junkfood.seal.ui.component.MaxTemplateNativeAdViewComposableLoader
import com.junkfood.seal.util.CUSTOM_COMMAND
import com.junkfood.seal.util.DownloadUtil
import com.junkfood.seal.util.FORMAT_SELECTION
import com.junkfood.seal.util.PLAYLIST
import com.junkfood.seal.util.PlaylistResult
import com.junkfood.seal.util.PreferenceUtil.getBoolean
import com.junkfood.seal.util.ToastUtil
import com.junkfood.seal.util.VideoInfo
import com.junkfood.seal.util.isYouTubeLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel

// TODO: Refactoring for introducing multitasking and download queue management
class DownloadViewModel @Inject constructor(
    private val repository: OfflineFirstRepository,
    private val appLovinSdk: AppLovinSdk,
    private val appLovinSdkInitialization: AppLovinSdkInitializationConfiguration
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = repository.userData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val mutableViewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = mutableViewStateFlow.asStateFlow()
    var currentPoints by mutableIntStateOf(0)
    val videoInfoFlow = MutableStateFlow(VideoInfo())
    private val nativeAdLoader: MaxTemplateNativeAdViewComposableLoader by lazy {
        MaxTemplateNativeAdViewComposableLoader()
    }
    val adState: StateFlow<AdViewState> get() = nativeAdLoader.nativeAdView
    private val _makeUpStateFlow = MutableStateFlow("")
    val makeUpStateFlow = _makeUpStateFlow.asStateFlow()
    data class ViewState(
        val showPlaylistSelectionDialog: Boolean = false,
        val url: String = "",
        val showFormatSelectionPage: Boolean = false,
        val isUrlSharingTriggered: Boolean = false,
    )

    fun updateUrl(url: String, isUrlSharingTriggered: Boolean = false) =
        mutableViewStateFlow.update {
            it.copy(
                url = url, isUrlSharingTriggered = isUrlSharingTriggered
            )
        }

    fun startDownloadVideo() {
        val url = viewStateFlow.value.url
        Downloader.clearErrorState()
        if (CUSTOM_COMMAND.getBoolean()) {
            applicationScope.launch(Dispatchers.IO) { DownloadUtil.executeCommandInBackground(url) }
            return
        }
        if (!Downloader.isDownloaderAvailable())
            return
        if (url.isBlank()) {
            ToastUtil.makeToast(context.getString(R.string.url_empty))
            return
        }
        if (url.isYouTubeLink()) {
            ToastUtil.makeToast(R.string.paste_youtube_fail_msg)
            return
        }
        if (PLAYLIST.getBoolean()) {
            viewModelScope.launch(Dispatchers.IO) { parsePlaylistInfo(url) }
            return
        }

        if (FORMAT_SELECTION.getBoolean()) {
            viewModelScope.launch(Dispatchers.IO) { fetchInfoForFormatSelection(url) }
            return
        }

        Downloader.getInfoAndDownload(url)
    }


    private fun fetchInfoForFormatSelection(url: String) {
        Downloader.updateState(State.FetchingInfo)
        DownloadUtil.fetchVideoInfoFromUrl(url = url).onSuccess {
            showFormatSelectionPageOrDownload(it)
        }.onFailure {
            manageDownloadError(th = it, url = url, isFetchingInfo = true, isTaskAborted = true)
        }
        Downloader.updateState(State.Idle)
    }


    private fun parsePlaylistInfo(url: String): Unit =
        Downloader.run {
            if (!isDownloaderAvailable()) return
            clearErrorState()
            updateState(State.FetchingInfo)
            DownloadUtil.getPlaylistOrVideoInfo(url).onSuccess { info ->
                updateState(State.Idle)
                when (info) {
                    is PlaylistResult -> {
                        showPlaylistPage(info)
                    }

                    is VideoInfo -> {
                        if (FORMAT_SELECTION.getBoolean()) {

                            showFormatSelectionPageOrDownload(info)
                        } else if (isDownloaderAvailable()) {
                            downloadVideoWithInfo(info = info)
                        }
                    }
                }
            }.onFailure {
                manageDownloadError(
                    th = it,
                    url = url,
                    isFetchingInfo = true,
                    isTaskAborted = true
                )
            }
        }

    private fun showPlaylistPage(playlistResult: PlaylistResult) {
        updatePlaylistResult(playlistResult)
        mutableViewStateFlow.update {
            it.copy(
                showPlaylistSelectionDialog = true,
            )
        }
    }

    private fun showFormatSelectionPageOrDownload(info: VideoInfo) {
        if (info.format.isNullOrEmpty())
            Downloader.downloadVideoWithInfo(info)
        else {
            videoInfoFlow.update { info }
            mutableViewStateFlow.update {
                it.copy(
                    showFormatSelectionPage = true,
                )
            }
        }
    }

    fun hidePlaylistDialog() {
        mutableViewStateFlow.update { it.copy(showPlaylistSelectionDialog = false) }
    }

    fun hideFormatPage() {
        mutableViewStateFlow.update { it.copy(showFormatSelectionPage = false) }
    }

    fun onShareIntentConsumed() {
        mutableViewStateFlow.update { it.copy(isUrlSharingTriggered = false) }
    }

    fun onNotSupportError(url: String) {
        notSupportError(url = url, errorReport = context.getString(R.string.paste_youtube_fail_msg))
    }

    val itemsSupport
        get() = listOf(
            SupportModel(
                "https://cdn-icons-png.flaticon.com/128/5968/5968764.png",
                0xFFEFF4FB,
                "Facebook"
            ),
            SupportModel(
                "https://cdn-icons-png.flaticon.com/128/3669/3669950.png",
                0xFFD3D3D3,
                "Tiktok"
            ),
            SupportModel(
                "https://cdn-icons-png.flaticon.com/128/3955/3955024.png",
                0xFFFBEFF7,
                "Instagram"
            ),
            SupportModel(
                "https://cdn-icons-png.flaticon.com/128/3670/3670151.png",
                0xFFEFFBF0,
                "Twitter"
            ),
        )

    fun resetPointsIfDaily(lastUpdated: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = lastUpdated
        val lastUpdatedDay = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.timeInMillis = System.currentTimeMillis()
        val currentDay = calendar.get(Calendar.DAY_OF_YEAR)

        if (currentDay != lastUpdatedDay) {
            // Nếu đã qua ngày mới, reset điểm và cập nhật ngày
            viewModelScope.launch {
                repository.setDownloadCount(5)
            }
            viewModelScope.launch {
                repository.setLastDay(System.currentTimeMillis())
            }
            Log.d(TAG, "Reset Points")
        }
    }

    fun addPoints(
        points: Int,
        currentPoints: Int
    ) {
        val newPoints = currentPoints + points
        viewModelScope.launch {
            repository.setDownloadCount(newPoints)
        }
    }

    fun deductPoints(
        points: Int,
        currentPoints: Int
    ): Boolean {
        if (currentPoints >= points) {
            viewModelScope.launch {
                repository.setDownloadCount(currentPoints - points)
            }
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        nativeAdLoader.destroy()
    }

    fun loadAds(
        context: Context,
        adUnitIdentifier: String
    ) {
        // Initialize ad with ad loader.
        if (SHOW_ADS) {
            appLovinSdk.initialize(appLovinSdkInitialization) {
                nativeAdLoader.loadAd(context, adUnitIdentifier)
                Log.d("Applovin", "loadAds")
            }
        }

    }

    fun makeUp(type: String?) {
        if (type != null) {
            _makeUpStateFlow.update {
                type
            }
        }
    }

    companion object {
        private const val TAG = "DownloadViewModel"
    }
}
