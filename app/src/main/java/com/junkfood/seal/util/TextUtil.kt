package com.junkfood.seal.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.text.isDigitsOnly
import com.junkfood.seal.App.Companion.applicationScope
import com.junkfood.seal.App.Companion.context
import com.junkfood.seal.R
import com.junkfood.seal.util.PreferenceUtil.getBoolean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.math.roundToInt

object ToastUtil {
    fun makeToast(text: String) {
        Toast.makeText(context.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun makeToastSuspend(text: String) {
        applicationScope.launch(Dispatchers.Main) {
            makeToast(text)
        }
    }

    fun makeToast(stringId: Int) {
        Toast.makeText(context.applicationContext, context.getString(stringId), Toast.LENGTH_SHORT)
            .show()
    }
}

private const val GIGA_BYTES = 1024f * 1024f * 1024f
private const val MEGA_BYTES = 1024f * 1024f

@Composable
fun Number?.toFileSizeText(): String {
    if (this == null) return stringResource(id = R.string.unknown)

    return this.toFloat().run {
        if (this > GIGA_BYTES)
            stringResource(R.string.filesize_gb).format(this / GIGA_BYTES)
        else stringResource(R.string.filesize_mb).format(this / MEGA_BYTES)
    }
}

/**
 * Convert time in **seconds** to `hh:mm:ss` or `mm:ss`
 */
fun Int.toDurationText(): String = this.run {
    if (this > 3600) "%d:%02d:%02d".format(this / 3600, (this % 3600) / 60, this % 60)
    else "%02d:%02d".format(this / 60, this % 60)
}

fun String.isNumberInRange(start: Int, end: Int): Boolean {
    return this.isNotEmpty() && this.isDigitsOnly() && this.length < 10 && this.toInt() >= start && this.toInt() <= end
}

fun String.isNumberInRange(range: IntRange): Boolean = this.isNumberInRange(range.first, range.last)

fun ClosedFloatingPointRange<Float>.toIntRange() =
    IntRange(start.roundToInt(), endInclusive.roundToInt())

fun String?.toHttpsUrl(): String =
    this?.run {
        if (matches(Regex("^(http:).*"))) replaceFirst("http", "https") else this
    } ?: ""


fun matchUrlFromClipboard(string: String, isMatchingMultiLink: Boolean = false): String {
    matchUrlFromString(string, isMatchingMultiLink).run {
        if (isEmpty())
            ToastUtil.makeToast(R.string.paste_fail_msg)
        else if (isYouTubeLink()) {
            ToastUtil.makeToast(R.string.paste_youtube_fail_msg)
        }
        else
            ToastUtil.makeToast(R.string.paste_msg)
        return this
    }
}

fun matchUrlFromSharedText(s: String): String {
    matchUrlFromString(s).run {
        if (isEmpty())
            ToastUtil.makeToast(R.string.share_fail_msg)
        else if (isYouTubeLink()) {
            ToastUtil.makeToast(R.string.paste_youtube_fail_msg)
        }
        return this
    }
}

fun matchUrlFromString(s: String, isMatchingMultiLink: Boolean = false): String {
    val builder = StringBuilder()
    val pattern =
        Pattern.compile("(http|https)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?")
    with(pattern.matcher(s)) {
        if (isMatchingMultiLink)
            while (find()) {
                if (builder.isNotEmpty())
                    builder.append("\n")
                builder.append(group())
            }
        else if (find())
            builder.append(group())
    }
    return builder.toString()
}


fun connectWithDelimiter(vararg strings: String?, delimiter: String): String =
    strings
        .toList()
        .filter { !it.isNullOrBlank() }
        .joinToString(separator = delimiter) { it.toString() }


fun connectWithBlank(s1: String, s2: String): String {
    val blank = if (s1.isEmpty() || s2.isEmpty()) "" else " "
    return s1 + blank + s2
}

fun String.isYouTubeLink(): Boolean {
    //if (DEBUG.getBoolean()) return false
    val pattern = Pattern.compile(
        "^(?:https?://)?(?:www\\.)?(?:(?:music\\.)?youtube\\.com/watch\\?v=\\S+|youtu\\.be/\\S+|youtube\\.com/shorts/\\S+)"
    )
    return pattern.matcher(this).matches()
}

fun String.isTikTokLink(): Boolean {
    val pattern = Pattern.compile("(?:https?://)?(?:www\\.)?(?:tiktok\\.com(?:/\\S+)?|vm\\.tiktok\\.com)(?:[/?]\\S*)?")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

