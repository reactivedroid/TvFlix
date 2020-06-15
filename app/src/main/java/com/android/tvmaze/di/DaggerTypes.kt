package com.android.tvmaze.di

/**
 * Sugar over multibindings that helps with Kotlin wildcards.
 */
typealias DaggerSet<T> = @JvmSuppressWildcards Set<T>
typealias DaggerMap<K, V> = @JvmSuppressWildcards Map<K, V>