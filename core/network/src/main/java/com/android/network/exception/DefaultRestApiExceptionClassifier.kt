package com.android.network.exception

import java.io.IOException
import java.net.SocketTimeoutException

internal class DefaultRestApiExceptionClassifier : RestApiExceptionClassifier {

    override fun classify(throwable: Throwable): Throwable {
        return when (throwable) {
            is SocketTimeoutException -> RestApiException.SocketTimeout(throwable)
            is IOException -> RestApiException.NetworkError(throwable)
            else -> throwable
        }
    }
}
