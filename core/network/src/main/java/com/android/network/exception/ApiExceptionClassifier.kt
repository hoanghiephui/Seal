package com.android.network.exception

import androidx.annotation.VisibleForTesting
import com.android.video.model.TiktokApiError
import kotlinx.serialization.json.Json
import retrofit2.HttpException

internal class ApiExceptionClassifier(
    private val parentRestApiExceptionClassifier: RestApiExceptionClassifier = DefaultRestApiExceptionClassifier()
) : RestApiExceptionClassifier {

    companion object {

        private const val MissingFieldExceptionCode = 10201
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun classify(throwable: Throwable): Throwable {
        return when (val parentClassifierResult =
            parentRestApiExceptionClassifier.classify(throwable)) {
            is HttpException -> parentClassifierResult.convertToApiException()
            else -> parentClassifierResult
        }
    }

    @VisibleForTesting
    internal fun String.decodeToApiError(): TiktokApiError {
        return json.decodeFromString(this)
    }

    private fun HttpException.convertToApiException(): Throwable {
        val body = response()?.errorBody()?.string() ?: ""
        return try {
            val error = body.decodeToApiError()
            when (error.httpStatusCode) {
                MissingFieldExceptionCode -> TiktokApiException.CommonError.MissingField(
                    this,
                    error
                )

                else -> TiktokApiException.UnknownError(this, error)
            }
        } catch (throwable: Throwable) {
            ApiDecodeException(throwable, body)
        }
    }
}
