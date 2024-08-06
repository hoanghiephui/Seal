package com.android.network.exception

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * This interface is extended from [CoroutineExceptionHandler] and provides a method to determine whether to handle the exception.
 */
interface ConditionalCoroutineExceptionHandler : CoroutineExceptionHandler {

    /**
     * Determine whether to handle the exception.
     */
    fun needsHandleException(coroutineContext: CoroutineContext, throwable: Throwable): Boolean
}
