package com.android.tvflix.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object FeatureFlagModule {
    @FavoritesFeatureFlag
    @Provides
    fun provideFavoritesFeatureFlag(appConfig: AppConfig): Boolean {
        return appConfig.getBoolean(ConfigDefaults.ConfigKeys.FEATURE_FAVORITES_ENABLE)
    }
}