package com.android.video.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TiktokApiError(
    @SerialName("statusCode")
    val httpStatusCode: Int,
    @SerialName("statusMsg")
    val statusMsg: String
) : Parcelable
