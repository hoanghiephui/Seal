package com.android.network.exception

/**
 * The classifier of rest api exceptions.
 */
interface RestApiExceptionClassifier {

    /**
     * Classify the throwable.
     *
     * If the [throwable] should be classified, return the classified new exception.
     * Otherwise, return the [throwable] itself.
     */
    fun classify(throwable: Throwable): Throwable
}
