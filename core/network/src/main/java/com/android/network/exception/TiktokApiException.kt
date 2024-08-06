package com.android.network.exception

import com.android.video.model.TiktokApiError


sealed class TiktokApiException(
    originalThrowable: Throwable,
    /**
     * The error details that server side responded.
     */
    val error: TiktokApiError
) : Exception(originalThrowable) {

    /**
     * Common error definition.
     *
     */
    sealed class CommonError(originalThrowable: Throwable, error: TiktokApiError) : TiktokApiException(originalThrowable, error) {


        class MissingField(originalThrowable: Throwable, error: TiktokApiError) : CommonError(originalThrowable, error)
    }

    /**
     * Specific error definition.
     *
     * Specific error is classified as require specific behavior.
     */
    sealed class SpecificError(originalThrowable: Throwable, error: TiktokApiError) : TiktokApiException(originalThrowable, error) {

        class AlreadyFinishedLiveViewingSession(
            originalThrowable: Throwable,
            error: TiktokApiError
        ) : SpecificError(originalThrowable, error)


        class UsServiceClosed(
            originalThrowable: Throwable,
            error: TiktokApiError
        ) : SpecificError(originalThrowable, error)
    }

    /**
     * Domain error definition.
     *
     */
    abstract class DomainError(originalThrowable: Throwable, error: TiktokApiError) : TiktokApiException(originalThrowable, error)


    class UnknownError(originalThrowable: Throwable, error: TiktokApiError) : TiktokApiException(originalThrowable, error)
}
