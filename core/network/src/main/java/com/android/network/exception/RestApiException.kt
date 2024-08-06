package com.android.network.exception

import java.io.IOException
import java.net.SocketTimeoutException

/**
 * An exception thrown when an error occurs in the REST API.
 */
sealed class RestApiException(originalThrowable: Throwable) : Exception(originalThrowable) {

    /**
     * An exception thrown when connection timeout occurs.
     */
    class SocketTimeout(originalThrowable: SocketTimeoutException) : RestApiException(originalThrowable)

    /**
     * An exception thrown when network error occurs.
     */
    class NetworkError(originalThrowable: IOException) : RestApiException(originalThrowable)
}
