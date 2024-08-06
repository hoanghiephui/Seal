package com.android.network

import com.android.network.internal.DefaultRestApiContainer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.properties.ReadOnlyProperty


interface RestApiContainer {

    /**
     * Get RestApi for accessing API
     */
    fun <TEndpoint> restApi(endpointClass: Class<TEndpoint>): ReadOnlyProperty<Any, RestApi<TEndpoint>>
}

@Module
@InstallIn(SingletonComponent::class)
internal interface RestApiContainerInternalModule {

    @Singleton
    @Binds
    fun bind(restApiContainer: DefaultRestApiContainer): RestApiContainer
}
