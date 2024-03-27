package com.junkfood.seal.ui.page.billing

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.billing.findActivity
import com.android.billingclient.api.Purchase
import com.junkfood.seal.R
import com.junkfood.seal.ui.component.AsyncLoadContents
import com.junkfood.seal.ui.component.BackButton
import com.junkfood.seal.ui.component.LargeTopAppBar
import com.junkfood.seal.ui.theme.bold
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingPage(
    onNavigateBack: () -> Unit,
    viewModel: BillingPlusViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    TitleItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }, navigationIcon = {
                    BackButton {
                        onNavigateBack()
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = { padding ->
            AsyncLoadContents(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                screenState = screenState,
                retryAction = {

                },
            ) { uiState ->
                BillingPlusDialog(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    onClickPurchase = {
                        scope.launch {
                            if (viewModel.purchase(context.findActivity())) {
                                onNavigateBack()
                            }
                        }
                    },
                    onClickVerify = {
                        scope.launch {
                            if (viewModel.verify(context)) {
                                onNavigateBack()
                            }
                        }
                    },
                    onClickConsume = {
                        scope.launch {
                            viewModel.consume(context, it)
                        }
                    },
                )
            }

        }
    )
}

const val PLAY_STORE_SUBSCRIPTION_URL =
    "https://play.google.com/store/account/subscriptions"

@Composable
private fun BillingPlusDialog(
    uiState: BillingPlusUiState,
    onClickPurchase: () -> Unit,
    onClickVerify: () -> Unit,
    onClickConsume: (Purchase) -> Unit,
    modifier: Modifier = Modifier,
) {
    val purchase = uiState.purchase
    val productDetails = uiState.productDetails
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.billing_plus_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        val price = productDetails?.rawProductDetails?.subscriptionOfferDetails?.first()
            ?.pricingPhases?.pricingPhaseList?.first()?.formattedPrice ?: "1,99 USD"
        Button(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(),
            onClick = { onClickPurchase.invoke() },
        ) {
            Text(stringResource(R.string.billing_plus_purchase_button, "$price **"))
        }

        OutlinedButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            onClick = { onClickVerify.invoke() },
        ) {
            Text(stringResource(R.string.billing_plus_verify_button))
        }

        if (purchase != null && uiState.isDeveloperMode) {
            OutlinedButton(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                onClick = { onClickConsume.invoke(purchase) },
            ) {
                Text(stringResource(R.string.billing_plus_consume_button))
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.billing_policy),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(PLAY_STORE_SUBSCRIPTION_URL)
                            context.startActivity(intent)
                        } catch (ex: Exception) {
                            Toast.makeText(
                                context,
                                "Something went wrong, please try again later.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text(text = "Open Payment & subscriptions")
                    }
                }

                PlusItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.billing_plus_item_metadata,
                    description = R.string.billing_plus_item_metadata_description,
                    icon = Icons.Default.WorkspacePremium,
                )

                PlusItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.billing_plus_item_download,
                    description = R.string.billing_plus_item_download_description,
                    icon = Icons.Default.Wallet,
                )

                PlusItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.billing_plus_item_material_you,
                    description = R.string.billing_plus_item_material_you_description,
                    icon = Icons.Default.DesignServices,
                )

                PlusItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.billing_plus_item_hide_ads,
                    description = R.string.billing_plus_item_hide_ads_description,
                    icon = Icons.Default.DoNotDisturb,
                )

                PlusItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.billing_plus_item_feature,
                    description = R.string.billing_plus_item_feature_description,
                    icon = Icons.Default.MoreHoriz,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                Color.Transparent,
                            ),
                        ),
                    ),
            )
        }
    }
}

@Composable
private fun TitleItem(modifier: Modifier = Modifier) {
    val titleStyle = MaterialTheme.typography.headlineLarge.bold()
    val annotatedString = buildAnnotatedString {
        append("Buy ")

        withStyle(titleStyle.copy(color = MaterialTheme.colorScheme.primary).toSpanStyle()) {
            append("Wallet+")
        }
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        style = titleStyle,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun PlusItem(
    @StringRes title: Int,
    @StringRes description: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = null,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun BillingPlusScreenPreview() {
    BillingPlusDialog(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        uiState = BillingPlusUiState(
            isDeveloperMode = true,
            isPlusMode = false,
        ),
        onClickPurchase = {},
        onClickVerify = {},
        onClickConsume = {},
    )
}
