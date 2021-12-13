package com.android.tvflix.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TvFlixApiModule {
    @Provides
    @Singleton
    fun provideTvFlixApi(
        retrofit: Retrofit
    ): TvFlixApi {
        return retrofit.create(TvFlixApi::class.java)
    }
}
