package com.android.network.exception

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext


fun eitherCoroutineExceptionHandler(vararg handlers: CoroutineExceptionHandler): ConditionalCoroutineExceptionHandler {
    return EitherCoroutineExceptionHandler(handlers)
}

internal class EitherCoroutineExceptionHandler(
    @get:VisibleForTesting
    internal val handlers: Array<out CoroutineExceptionHandler>
) : ConditionalCoroutineExceptionHandler, CoroutineExceptionHandler by baseCoroutineExceptionHandler(handlers) {

    companion object {

        private fun needHandleException(
            handlers: Array<out CoroutineExceptionHandler>,
            coroutineContext: CoroutineContext,
            throwable: Throwable
        ): Boolean {
            return handlers.any {
                if (it is ConditionalCoroutineExceptionHandler) {
                    it.needsHandleException(coroutineContext, throwable)
                } else {
                    true
                }
            }
        }

        private fun handleException(
            handlers: Array<out CoroutineExceptionHandler>,
            coroutineContext: CoroutineContext,
            throwable: Throwable
        ) {
            for (handler in handlers) {
                if (handler is ConditionalCoroutineExceptionHandler) {
                    if (handler.needsHandleException(coroutineContext, throwable)) {
                        handler.handleException(coroutineContext, throwable)
                        return
                    }
                } else {
                    handler.handleException(coroutineContext, throwable)
                    return
                }
            }
        }

        private fun baseCoroutineExceptionHandler(
            handlers: Array<out CoroutineExceptionHandler>
        ): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { coroutineContext, throwable ->
                if (needHandleException(handlers, coroutineContext, throwable)) {
                    handleException(handlers, coroutineContext, throwable)
                }
            }
        }
    }

    override fun needsHandleException(coroutineContext: CoroutineContext, throwable: Throwable): Boolean {
        return needHandleException(handlers, coroutineContext, throwable)
    }
}
