package com.android.ashwiask.tvmaze.di;

import android.app.Application;

import com.android.ashwiask.tvmaze.TvMazeApplication;
import com.android.ashwiask.tvmaze.network.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Ashwini Kumar.
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuildersModule.class,
        NetworkModule.class,
        TvMazeDbModule.class})
public interface AppComponent {
    void inject(TvMazeApplication tvMazeApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
