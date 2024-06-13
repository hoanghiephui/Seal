package com.junkfood.seal.di

import android.content.Context
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.junkfood.seal.R
import com.junkfood.seal.repository.OfflineFirstRepository
import com.junkfood.seal.repository.OfflineFirstRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: OfflineFirstRepositoryImpl,
    ): OfflineFirstRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplovin(
        @ApplicationContext
        context: Context
    ): AppLovinSdk =
        AppLovinSdk.getInstance(context)

    @Provides
    @Singleton
    fun provideSdkInitialization(
        @ApplicationContext
        context: Context
    ): AppLovinSdkInitializationConfiguration =
        AppLovinSdkInitializationConfiguration.builder(
            context.getString(R.string.APPLOVIN_SDK_KEY),
            context
        )
            .setMediationProvider(AppLovinMediationProvider.MAX)
            .build()
}
