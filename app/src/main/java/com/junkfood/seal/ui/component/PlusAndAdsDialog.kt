package com.junkfood.seal.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R

@Composable
fun PlusAndAdsDialog(
    onDismissRequest: () -> Unit = {},
    onMakePlus: () -> Unit = {},
    onViewAds: () -> Unit = {},
) {
    SealDialogVariant(
        onDismissRequest = onDismissRequest,
        icon = {
            Image(
                painterResource(id = R.drawable.ic_pro),
                contentDescription = null
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.add_points),
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )
        },
        title = { Text(text = stringResource(id = R.string.package_basic)) },
        buttons = {
            SealDialogButtonVariant(
                text = stringResource(id = R.string.with_sub),
                shape = TopButtonShape
            ) {
                onMakePlus()
                onDismissRequest.invoke()
            }
            SealDialogButtonVariant(
                text = stringResource(id = R.string.with_ads),
                shape = BottomButtonShape
            ) {
                onViewAds()
                onDismissRequest.invoke()
            }
        },
    )
}

@Preview
@Composable
fun PreviewPlusAndAdsDialog() {
    PlusAndAdsDialog()
}
