package com.android.ashwiask.tvmaze;

import com.android.ashwiask.tvmaze.home.HomeComponent;
import com.android.ashwiask.tvmaze.home.HomeModule;
import com.android.ashwiask.tvmaze.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Ashwini Kumar.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent
{
    HomeComponent plus(HomeModule homeModule);
}
