package com.android.network.cleaner

import javax.inject.Qualifier

/**
 * A qualifier annotation for inject [CompositeDataLifespanCleaner] as an instance of [DataLifecycleCleaner]
 *
 * Bound instance is all data cleaner for [DataLifespan].
 * (Ex: MemoryStorage, FileSystem, DataStore, ...)
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CompositeDataLifespanCleaner
