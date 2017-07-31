package com.android.ashwiask.tvmaze;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ashwini Kumar.
 */
@Module
public class AppModule
{
    private TvMazeApplication application;

    public AppModule(TvMazeApplication app)
    {
        application = app;
    }

    @Provides
    @Singleton
    Context provideContext()
    {
        return application;
    }

    @Provides
    Application provideApplication()
    {
        return application;
    }
}
