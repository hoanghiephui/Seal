package com.android.network.internal

import com.android.network.EntireTimeout
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Invocation

/**
 * This class is Call.Factory to set the entire timeout for a specific endpoint.
 * If you want to set the timeout, you need to add an annotation for each endpoint.
 *
 * @see EntireTimeout
 */
internal class TimeoutCallFactory(val client: OkHttpClient) : Call.Factory {
    override fun newCall(request: Request): Call {
        return client.newCall(request)
            .apply {
                val method = request.tag(Invocation::class.java)?.method()
                method?.getAnnotation(EntireTimeout::class.java)?.let {
                    timeout().timeout(it.value, it.unit)
                }
            }
    }
}
