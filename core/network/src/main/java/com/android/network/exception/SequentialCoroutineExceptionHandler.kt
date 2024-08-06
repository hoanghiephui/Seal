package com.android.network.exception

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * This method is composed of all [handlers].
 * [handlers] are executed in the order they are passed.
 */
fun sequentialCoroutineExceptionHandler(vararg handlers: CoroutineExceptionHandler): CoroutineExceptionHandler {
    return SequentialCoroutineExceptionHandler(handlers)
}

internal class SequentialCoroutineExceptionHandler(
    @get:VisibleForTesting
    internal val handlers: Array<out CoroutineExceptionHandler>
) : CoroutineExceptionHandler by baseCoroutineExceptionHandler(handlers) {

    companion object {

        private fun baseCoroutineExceptionHandler(handlers: Array<out CoroutineExceptionHandler>): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { context, throwable ->
                for (handler in handlers) {
                    handler.handleException(context, throwable)
                }
            }
        }
    }
}
