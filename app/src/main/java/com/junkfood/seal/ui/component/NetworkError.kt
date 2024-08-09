package com.junkfood.seal.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.junkfood.seal.R

@Composable
fun NetworkError(
    modifier: Modifier = Modifier,
    onClickReload: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        /*Image(
            painter = painterResource(id = CoreDataResourceDrawable.icon_error_status),
            contentDescription = null
        )*/

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.error_executed),
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.W300
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onClickReload,
        ) {
            Text(text = stringResource(id = R.string.common_reload))
        }
    }
}
