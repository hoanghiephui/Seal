package com.junkfood.seal.database.backup

import android.content.Context
import android.text.format.DateFormat
import com.junkfood.seal.App
import com.junkfood.seal.R
import com.junkfood.seal.database.objects.CommandTemplate
import com.junkfood.seal.database.objects.DownloadedVideoInfo
import com.junkfood.seal.database.objects.OptionShortcut
import com.junkfood.seal.util.DatabaseUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

object BackupUtil {
    private val format = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    suspend fun exportTemplatesToJson() =
        exportTemplatesToJson(
            templates = DatabaseUtil.getTemplateList(),
            shortcuts = DatabaseUtil.getShortcutList()
        )

    fun exportTemplatesToJson(
        templates: List<CommandTemplate>,
        shortcuts: List<OptionShortcut>
    ): String {
        return format.encodeToString(
            Backup(
                templates = templates, shortcuts = shortcuts
            )
        )
    }

    fun List<DownloadedVideoInfo>.toJsonString(): String {
        return format.encodeToString(Backup(downloadHistory = this))
    }

    fun List<DownloadedVideoInfo>.toURLListString(): String {
        return this.map { it.videoUrl }.joinToString(separator = "\n") { it }
    }

    fun String.decodeToBackup(): Result<Backup> {
        return format.runCatching {
            decodeFromString<Backup>(this@decodeToBackup)
        }
    }

    fun getDownloadHistoryExportFilename(context: Context): String {
        return listOf(
            context.getString(R.string.app_name),
            App.packageInfo.versionName,
            Date().toString()
        ).joinToString(separator = "-") { it.toString() }
    }

    enum class BackupDestination {
        File, Clipboard
    }

    enum class BackupType {
        DownloadHistory, URLList, CommandTemplate, CommandShortcut
    }
}
