package com.android.network.internal.interceptor

/**
 * An annotation to mark cacheable, this annotation is used for Retrofit.
 * If you mark this annotation, retrofit will cache the network response.
 *
 * ```kt
 * interface ExampleEndpoint {
 *
 *     @Cacheable
 *     @GET("/example")
 *     suspend fun example(): Response<Example>
 * }
 * ```
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Cacheable
