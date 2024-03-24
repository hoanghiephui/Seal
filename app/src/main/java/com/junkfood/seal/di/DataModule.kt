package com.junkfood.seal.di

import com.junkfood.seal.repository.OfflineFirstRepository
import com.junkfood.seal.repository.OfflineFirstRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstRepositoryImpl,
    ): OfflineFirstRepository
}