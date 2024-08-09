package com.junkfood.seal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * ParcelableResult is similar to Kotlin's [Result] class
 */
sealed class ParcelableResult<T : Parcelable> : Parcelable {

    /**
     * Represents a successful result of type [T]
     */
    @Parcelize
    data class Success<T : Parcelable>(override val result: T) : ParcelableResult<T>()

    /**
     * Represents a failed result
     */
    @Parcelize
    class Failure<T : Parcelable> : ParcelableResult<T>()

    /**
     * Returns the result if this is a [Success] instance, otherwise null
     */
    open val result: T?
        get() = (this as? Success)?.result

    /**
     * Convenience method to copy the result if this is a [Success] instance
     */
    fun copyIfSuccess(copy: (T) -> T): ParcelableResult<T> {
        return if (this is Success) {
            this.copy(result = copy(this.result))
        } else {
            this
        }
    }
}
