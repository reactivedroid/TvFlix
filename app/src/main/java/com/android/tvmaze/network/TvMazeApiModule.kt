package com.android.tvmaze.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

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
