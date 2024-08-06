package com.android.video.data.di

import com.android.video.data.repository.TiktokExploreRepository
import com.android.video.data.repository.internal.TiktokRestApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bind(tiktokRestApi: TiktokRestApi): TiktokExploreRepository
}
