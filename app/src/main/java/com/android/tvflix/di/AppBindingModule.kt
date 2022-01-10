package com.android.tvflix.di

import com.android.tvflix.analytics.Analytics
import com.android.tvflix.analytics.FirebaseAnalyticsHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppBindingModule {
    @Binds
    abstract fun bindFirebaseAnalytics(firebaseAnalyticsHandler: FirebaseAnalyticsHandler): Analytics
}