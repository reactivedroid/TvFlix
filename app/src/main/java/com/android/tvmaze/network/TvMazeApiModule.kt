package com.android.tvmaze.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object TvMazeApiModule {
    @Provides
    @Singleton
    fun provideTvMazeApi(
        retrofit: Retrofit
    ): TvMazeApi {
        return retrofit.create(TvMazeApi::class.java)
    }
}
