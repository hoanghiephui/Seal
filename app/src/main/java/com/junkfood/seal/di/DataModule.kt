package com.junkfood.seal.di

import android.content.Context
import com.applovin.sdk.AppLovinSdk
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
abstract class DataModule {
    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstRepositoryImpl,
    ): OfflineFirstRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {
    @Provides
    @Singleton
    fun provideApplovin(
        @ApplicationContext
        context: Context
    ): AppLovinSdk =
        AppLovinSdk.getInstance(context)
}