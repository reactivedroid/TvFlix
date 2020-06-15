package com.android.tvmaze.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
abstract class InterceptorModule {
    @Binds
    @IntoSet
    abstract fun bindApiInterceptor(apiInterceptor: ApiInterceptor): Interceptor

    @Binds
    @IntoSet
    abstract fun bindHttpLoggingInterceptor(httpLoggingInterceptor: HttpLoggingInterceptor): Interceptor

    @Binds
    @IntoSet
    abstract fun bindChuckerInterceptor(chuckerInterceptor: ChuckerInterceptor): Interceptor
}