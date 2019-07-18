package com.android.ashwiask.tvmaze.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Ashwini Kumar.
 */
@Module(includes = [ViewModelModule::class])
internal class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }
}
