package com.android.network.internal.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

internal class CacheControlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val method = request.tag(Invocation::class.java)?.method()
        if (method?.getAnnotation(Cacheable::class.java) == null) {
            builder.cacheControl(CacheControl.FORCE_NETWORK)
        }
        return chain.proceed(builder.build())
    }
}
