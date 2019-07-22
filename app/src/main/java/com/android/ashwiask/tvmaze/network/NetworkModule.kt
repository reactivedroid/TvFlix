package com.android.ashwiask.tvmaze.network

import android.content.Context
import com.android.ashwiask.tvmaze.network.backend.TvMazeApiModule
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [TvMazeApiModule::class])
class NetworkModule {

    @Provides
    @Named(TVMAZE_BASE_URL)
    internal fun provideBaseUrlString(): String {
        return BASE_URL
    }

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
        val cacheSize = 5 * 1024 * 1024 // 5 MB
        val cacheDir = context.cacheDir
        return Cache(cacheDir, cacheSize.toLong())
    }

    companion object {
        const val TVMAZE_BASE_URL = "tvmaze_base_url"
        private const val BASE_URL = "https://api.tvmaze.com/"
    }
}
