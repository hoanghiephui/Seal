package com.android.network.exception


class ApiDecodeException(
    originalThrowable: Throwable,
    /**
     * The original body that failed to be decoded.
     */
    val errorBody: String
) : Exception(originalThrowable)
