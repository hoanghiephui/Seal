package com.android.network.internal

import com.android.network.RestApi
import com.android.network.RestApiContainer
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class DefaultRestApiContainer @Inject constructor(
    private val restApiPool: RestApiPool,
) : RestApiContainer {

    private class RestApiProperty<TEndpoint>(
        private val createRestApi: () -> RestApi<TEndpoint>
    ) : ReadOnlyProperty<Any, RestApi<TEndpoint>> {

        override fun getValue(thisRef: Any, property: KProperty<*>): RestApi<TEndpoint> {
            return createRestApi()
        }
    }

    override fun <TEndpoint> restApi(endpointClass: Class<TEndpoint>): ReadOnlyProperty<Any, RestApi<TEndpoint>> {
        return RestApiProperty { AppRestApi(restApiPool, endpointClass) }
    }
}
