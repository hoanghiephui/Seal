package com.junkfood.seal.ui.page.download

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junkfood.seal.App.Companion.applicationScope
import com.junkfood.seal.App.Companion.context
import com.junkfood.seal.Downloader
import com.junkfood.seal.Downloader.State
import com.junkfood.seal.Downloader.manageDownloadError
import com.junkfood.seal.Downloader.notSupportError
import com.junkfood.seal.Downloader.updatePlaylistResult
import com.junkfood.seal.R
import com.junkfood.seal.SupportModel
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

// TODO: Refactoring for introducing multitasking and download queue management
class DownloadViewModel @Inject constructor() : ViewModel() {


    private val mutableViewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = mutableViewStateFlow.asStateFlow()

    val videoInfoFlow = MutableStateFlow(VideoInfo())

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
        if(url.isYouTubeLink()) {
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

    val itemsSupport get() = listOf(
        SupportModel("https://cdn-icons-png.flaticon.com/128/5968/5968764.png", 0xFFEFF4FB, "Facebook"),
        SupportModel("https://cdn-icons-png.flaticon.com/128/3669/3669950.png", 0xFFD3D3D3, "Tiktok"),
        SupportModel("https://cdn-icons-png.flaticon.com/128/3955/3955024.png", 0xFFFBEFF7, "Instagram"),
        SupportModel("https://cdn-icons-png.flaticon.com/128/3670/3670151.png", 0xFFEFFBF0, "Twitter"),
    )

    companion object {
        private const val TAG = "DownloadViewModel"
    }
}
