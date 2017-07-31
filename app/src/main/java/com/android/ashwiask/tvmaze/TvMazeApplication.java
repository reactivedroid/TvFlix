package com.android.ashwiask.tvmaze;

import android.app.Application;
import android.content.Context;

import com.android.ashwiask.tvmaze.network.NetworkModule;


/**
 * @author Ashwini Kumar.
 */
public class TvMazeApplication extends Application
{
    private AppComponent appComponent;

    public static TvMazeApplication get(Context context)
    {
        return (TvMazeApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        initAppComponent();
    }

    private void initAppComponent()
    {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public AppComponent getAppComponent()
    {
        return appComponent;
    }
}
