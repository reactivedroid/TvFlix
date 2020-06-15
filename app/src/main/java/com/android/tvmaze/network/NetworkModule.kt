package com.android.tvmaze.network

import android.content.Context
import android.os.Looper
import com.android.tvmaze.BuildConfig
import com.android.tvmaze.di.DaggerSet
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi

@Module(includes = [TvMazeApiModule::class, InterceptorModule::class])
object NetworkModule {
    const val TVMAZE_BASE_URL = "tvmaze_base_url"
    private const val BASE_URL = "https://api.tvmaze.com/"

    @Provides
    @Named(TVMAZE_BASE_URL)
    fun provideBaseUrlString(): String {
        return BASE_URL
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    @Provides
    @Singleton
    fun provideChuckInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    // Use newBuilder() to customize so that thread-pool and connection-pool same are used
    @Provides
    fun provideOkHttpClientBuilder(
        @InternalApi okHttpClient: Lazy<OkHttpClient>
    ): OkHttpClient.Builder {
        return okHttpClient.get().newBuilder()
    }

    @InternalApi
    @Provides
    @Singleton
    fun provideBaseOkHttpClient(
        interceptors: DaggerSet<Interceptor>,
        cache: Cache
    ): OkHttpClient {
        check(Looper.myLooper() != Looper.getMainLooper()) { "HTTP client initialized on main thread." }
        val builder = OkHttpClient.Builder()
        builder.interceptors()
            .addAll(interceptors)
        builder.cache(cache)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideCache(context: Context): Cache {
        check(Looper.myLooper() != Looper.getMainLooper()) { "Cache initialized on main thread." }
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cacheDir = context.cacheDir
        return Cache(cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @InternalApi
        okHttpClient: Lazy<OkHttpClient>,
        @Named(TVMAZE_BASE_URL) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .callFactory { okHttpClient.get().newCall(it) }
            .build()
    }
}
