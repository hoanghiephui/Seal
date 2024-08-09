package com.junkfood.seal.ui.page.v2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.junkfood.seal.R
import com.junkfood.seal.ui.page.download.DownloadViewModel
import com.junkfood.seal.ui.page.v2.section.DownloadInitialSection
import com.junkfood.seal.ui.page.v2.section.DownloadInitialSectionState
import com.junkfood.seal.ui.page.v2.section.DownloadLoadedSection
import com.junkfood.seal.ui.page.v2.section.DownloadLoadedSectionState
import com.junkfood.seal.ui.page.v2.section.DownloadLoadingSection
import com.junkfood.seal.ui.page.v2.section.DownloadLoadingSectionState

@Composable
fun DownloadPageHost(
    downloadViewModel: DownloadViewModel,
    navHostController: NavHostController,
    onNavigateToCookieGeneratorPage: (String) -> Unit = {},
    onViewAds: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    LaunchedEffect(downloadViewModel) {
        downloadViewModel.initialLoadIfNeeded(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
        )

    }

    val pageState = rememberDownloadPageState(
        downloadViewModel = downloadViewModel,
        navigator = navHostController,
        onNavToCookieGeneratorPage = onNavigateToCookieGeneratorPage,
        onViewAds = onViewAds,
        screenHeight = screenHeight,
        screenWidth = screenWidth
    )
    DownloadPage(
        pageState = pageState
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun DownloadPage(
    pageState: DownloadPageState
) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    var backdropRevealed by rememberSaveable { mutableStateOf(scaffoldState.isRevealed) }
    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
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
                            pageState.navigateToSettings()
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
                TooltipBox(state = rememberTooltipState(),
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = stringResource(id = R.string.downloads_history))
                        }
                    }) {
                    IconButton(
                        onClick = {
                            pageState.navigateToDownloads()
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
        }) { padding ->
        when (val sectionState = pageState.contentSectionState) {
            is DownloadLoadingSectionState -> DownloadLoadingSection()

            is DownloadLoadedSectionState -> DownloadLoadedSection(
                sectionState = sectionState,
                padding = padding
            )
            is DownloadInitialSectionState -> DownloadInitialSection(
                sectionState = sectionState,
                padding = padding
            )
        }

    }
}

