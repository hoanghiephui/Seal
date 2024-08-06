package com.android.network

import com.android.network.exception.RestApiExceptionClassifier

/**
 * A REST API interface for specific endpoint
 */
interface RestApi<TEndpoint> {

    /**
     * Call API
     * @param restApiExceptionClassifier Custom exception classifier for this API endpoint
     */
    suspend fun <TResult> callApi(
        restApiExceptionClassifier: RestApiExceptionClassifier? = null,
        execute: suspend TEndpoint.() -> TResult
    ): TResult
}
