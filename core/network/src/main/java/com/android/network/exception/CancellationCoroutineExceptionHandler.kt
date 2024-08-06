package com.android.network.exception

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * provide coroutine exception handler for [CancellationException]
 */
fun cancellationCoroutineExceptionHandler(): ConditionalCoroutineExceptionHandler {
    return CancellationCoroutineExceptionHandler()
}

internal class CancellationCoroutineExceptionHandler :
    ConditionalCoroutineExceptionHandler, CoroutineExceptionHandler by baseCoroutineExceptionHandler() {

    companion object {

        private fun needHandleException(throwable: Throwable): Boolean {
            return throwable is CancellationException
        }

        private fun baseCoroutineExceptionHandler(): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, throwable ->
                @Suppress("ControlFlowWithEmptyBody")
                if (needHandleException(throwable)) {
                    // Do nothing because this exception should be ignored for cancelling coroutine
                }
            }
        }
    }

    override fun needsHandleException(coroutineContext: CoroutineContext, throwable: Throwable): Boolean {
        return needHandleException(throwable)
    }
}
