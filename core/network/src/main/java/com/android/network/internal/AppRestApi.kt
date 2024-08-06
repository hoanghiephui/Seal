package com.android.network.internal

import androidx.annotation.VisibleForTesting
import com.android.network.RestApi
import com.android.network.exception.ApiExceptionClassifier
import com.android.network.exception.RestApiExceptionClassifier
import java.util.concurrent.CancellationException

internal class AppRestApi<T>(
    private val restApiPool: RestApiPool,
    private val endpointClass: Class<T>,
    @get:VisibleForTesting
    internal val apiExceptionClassifier: ApiExceptionClassifier = ApiExceptionClassifier()
) : RestApi<T> {

    override suspend fun <TResult> callApi(
        restApiExceptionClassifier: RestApiExceptionClassifier?,
        execute: suspend T.() -> TResult
    ): TResult {
        val endpoint = restApiPool.getOrCreateEndpoint(endpointClass)
        try {
            return endpoint.execute()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            var rethrow = apiExceptionClassifier.classify(e)
            if (restApiExceptionClassifier != null) {
                rethrow = restApiExceptionClassifier.classify(rethrow)
            }
            throw rethrow
        }
    }
}
