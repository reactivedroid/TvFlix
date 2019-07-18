package com.android.ashwiask.tvmaze.di

import android.app.Application
import com.android.ashwiask.tvmaze.TvMazeApplication
import com.android.ashwiask.tvmaze.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author Ashwini Kumar.
 */
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBuildersModule::class, NetworkModule::class, TvMazeDbModule::class])
interface AppComponent {
    fun inject(tvMazeApplication: TvMazeApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
