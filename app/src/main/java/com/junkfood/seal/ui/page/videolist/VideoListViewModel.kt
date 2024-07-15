package com.junkfood.seal.ui.page.videolist

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.junkfood.seal.R
import com.junkfood.seal.SHOW_ADS
import com.junkfood.seal.database.backup.BackupUtil
import com.junkfood.seal.database.backup.BackupUtil.decodeToBackup
import com.junkfood.seal.database.objects.DownloadedVideoInfo
import com.junkfood.seal.ui.component.AdViewState
import com.junkfood.seal.ui.component.MaxTemplateNativeAdViewComposableLoader
import com.junkfood.seal.util.DatabaseUtil
import com.junkfood.seal.util.FileUtil.getFileSize
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "VideoListViewModel"

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val appLovinSdk: AppLovinSdk,
    private val appLovinSdkInitialization: AppLovinSdkInitializationConfiguration,
    @ApplicationContext
    val context: Context
) : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(VideoListViewState())
    val stateFlow = mutableStateFlow.asStateFlow()
    private val viewState get() = stateFlow.value
    private val nativeAdLoader: MaxTemplateNativeAdViewComposableLoader by lazy {
        MaxTemplateNativeAdViewComposableLoader()
    }
    val adState: StateFlow<AdViewState> get() = nativeAdLoader.nativeAdView
    private val _mediaInfoFlow = DatabaseUtil.getDownloadHistoryFlow()

    val videoListFlow: Flow<List<DownloadedVideoInfo>> =
        _mediaInfoFlow.map { it.reversed().sortedBy { info -> info.filterByType() } }

    val searchedVideoListFlow = videoListFlow.combine(stateFlow) { list, state ->
        if (!state.isSearching || state.searchText.isBlank()) list
        else list.filter {
            state.searchText.let { text ->
                with(it) {
                    videoTitle.contains(text, ignoreCase = true) || videoAuthor.contains(
                        text,
                        ignoreCase = true
                    ) || extractor.contains(text, ignoreCase = true) || videoPath.contains(
                        text,
                        ignoreCase = true
                    )
                }
            }
        }
    }

    val filterSetFlow = searchedVideoListFlow.map { infoList ->
        mutableSetOf<String>().apply {
            infoList.forEach {
                this.add(it.extractor)
            }
        }
    }

    val fileSizeMapFlow = videoListFlow.flowOn(Dispatchers.IO).map { list ->
        list.associate {
            it.id to it.videoPath.getFileSize(context = context)
        }
    }


    fun clickVideoFilter() {
        if (mutableStateFlow.value.videoFilter) mutableStateFlow.update { it.copy(videoFilter = false) }
        else mutableStateFlow.update { it.copy(videoFilter = true, audioFilter = false) }
    }

    fun clickAudioFilter() {
        if (mutableStateFlow.value.audioFilter) mutableStateFlow.update { it.copy(audioFilter = false) }
        else mutableStateFlow.update { it.copy(audioFilter = true, videoFilter = false) }
    }

    fun clickExtractorFilter(index: Int) {
        if (mutableStateFlow.value.activeFilterIndex == index) mutableStateFlow.update {
            it.copy(
                activeFilterIndex = -1
            )
        }
        else mutableStateFlow.update { it.copy(activeFilterIndex = index) }
    }

    fun toggleSearch(isSearching: Boolean = !viewState.isSearching) {
        mutableStateFlow.update { it.copy(isSearching = isSearching, searchText = "") }
    }

    fun updateSearchText(text: String) {
        mutableStateFlow.update { it.copy(searchText = text) }
    }

    fun deleteDownloadHistory(infoList: List<DownloadedVideoInfo>, deleteFile: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseUtil.deleteInfoList(infoList = infoList, deleteFile = deleteFile, context = context)
        }
    }

    fun importBackupFromUri(
        context: Context, uri: Uri, onComplete: suspend (Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var res = 0
            context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader(Charsets.UTF_8).readText().let {
                    res = importBackupFromText(it)
                }
            }
            withContext(Dispatchers.Main) {
                onComplete(res)
            }
        }
    }

    fun importBackupFromText(
        string: String, onComplete: suspend (Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = importBackupFromText(string)
            withContext(Dispatchers.Main) {
                onComplete(res)
            }
        }
    }

    private suspend fun importBackupFromText(string: String): Int {
        string.decodeToBackup().onSuccess {
            return DatabaseUtil.importBackup(
                backup = it, types = setOf(BackupUtil.BackupType.DownloadHistory)
            )
        }
        return 0
    }

    fun showImportedSnackbar(hostState: SnackbarHostState, context: Context, importedCount: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            hostState.showSnackbar(
                message = context.getString(R.string.download_history_imported).format(
                    context.resources.getQuantityString(
                        R.plurals.item_count, importedCount
                    ).format(importedCount)
                )
            )
        }
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

    data class VideoListViewState(
        val activeFilterIndex: Int = -1,
        val videoFilter: Boolean = false,
        val audioFilter: Boolean = false,
        val isSearching: Boolean = false,
        val searchText: String = "",
    )

}
