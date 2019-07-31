package com.android.tvmaze.di

import com.android.tvmaze.favorite.FavoriteShowsActivity
import com.android.tvmaze.home.HomeActivity
import com.android.tvmaze.shows.AllShowsActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun bindAllShowsActivity(): AllShowsActivity

    @ContributesAndroidInjector
    abstract fun bindFavoriteShowsActivity(): FavoriteShowsActivity
}
