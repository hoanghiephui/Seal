package com.junkfood.seal.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.ui.theme.SealTheme
import kotlinx.coroutines.delay

@Composable
fun SealSearchBar(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    text: String,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }
    Surface(
        modifier = modifier
            .widthIn(360.dp, 720.dp)
            .height(56.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            SealTextField(
                value = text,
                onValueChange = onValueChange,
                placeholder = { Text(text = stringResource(R.string.search_in_downloads)) },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester = focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }), contentDescription = stringResource(id = R.string.search)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    var text by remember { mutableStateOf("") }
    SealTheme {
        Surface {
            SealSearchBar(text = text) { text = it }
        }

    }
}