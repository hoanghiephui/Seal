package com.android.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import javax.inject.Singleton

/**
 * The pool to manage http cache.
 */
interface HttpCachePool {

    /**
     * get or create http cache.
     *
     * in first instantiate, this method create instance.
     * in second or later instantiate, this method return cached instance.
     */
    suspend fun getOrCreateCache(): Cache
}


@Module
@InstallIn(SingletonComponent::class)
object HttpCachePoolModule {

    @Provides
    @Singleton
    fun provideHttpCacheRepository(
        @ApplicationContext context: Context
    ): HttpCachePool {
        return HttpCacheFileSystem(
            context = context
        )
    }
}
