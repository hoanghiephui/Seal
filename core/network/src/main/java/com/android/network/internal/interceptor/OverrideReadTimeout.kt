package com.android.network.internal.interceptor

/**
 * An annotation to override read timeout, this annotation is used for Retrofit.
 * If you want to override read timeout, you can use this annotation.
 *
 * default timeout is 60 seconds.
 * ```kt
 * interface ExampleEndpoint {
 *
 *     @OverrideReadTimeout
 *     @GET("/example")
 *     suspend fun example(): Response<Example>
 * }
 * ```
 *
 * or, you can set timeout seconds.
 * ```kt
 * interface ExampleEndpoint {
 *
 *     @OverrideReadTimeout(timeoutSeconds = 120)
 *     @GET("/example")
 *     suspend fun example(): Response<Example>
 * }
 * ```
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class OverrideReadTimeout(
    /**
     * timeout seconds.
     */
    val timeoutSeconds: Int = 60
)
