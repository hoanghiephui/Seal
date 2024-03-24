package com.junkfood.seal.ui.page.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.junkfood.seal.R
import com.mukesh.MarkDown
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportedSite(
    onBackPressed: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.supported_site),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                )
            }, scrollBehavior = scrollBehavior, navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(Icons.Outlined.Close, stringResource(R.string.close))
                }
            })
        }) { paddingValues ->

        MarkDown(
            modifier = Modifier.padding(paddingValues),
            url = URL("https://raw.githubusercontent.com/hoanghiephui/Seal/reup/supportedsites.md")
        )
    }
}