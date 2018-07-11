package com.android.ashwiask.tvmaze.di;

import com.android.ashwiask.tvmaze.favorite.FavoriteShowsActivity;
import com.android.ashwiask.tvmaze.home.HomeActivity;
import com.android.ashwiask.tvmaze.shows.AllShowsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract HomeActivity bindHomeActivity();

    @ContributesAndroidInjector
    abstract AllShowsActivity bindAllShowsActivity();

    @ContributesAndroidInjector
    abstract FavoriteShowsActivity bindFavoriteShowsActivity();
}
