package com.android.tvmaze.di

import android.app.Application
import com.android.tvmaze.TvMazeApplication
import com.android.tvmaze.db.TvMazeDbModule
import com.android.tvmaze.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuildersModule::class,
        NetworkModule::class,
        TvMazeDbModule::class]
)
interface AppComponent {
    fun inject(tvMazeApplication: TvMazeApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
