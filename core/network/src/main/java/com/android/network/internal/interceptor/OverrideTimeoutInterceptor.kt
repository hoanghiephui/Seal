package com.android.network.internal.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.util.concurrent.TimeUnit

internal class OverrideTimeoutInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var result = chain
        val request = chain.request()
        val method = request.tag(Invocation::class.java)?.method()
        val overrideReadTimeout = method?.getAnnotation(OverrideReadTimeout::class.java)
        if (overrideReadTimeout != null) {
            result = chain.withReadTimeout(overrideReadTimeout.timeoutSeconds, TimeUnit.SECONDS)
        }
        return result.proceed(request)
    }
}
