package com.android.tvmaze.di

import com.android.tvmaze.favorite.FavoriteShowsActivity
import com.android.tvmaze.favorite.FavoriteShowsModule
import com.android.tvmaze.home.HomeActivity
import com.android.tvmaze.home.HomeModule
import com.android.tvmaze.shows.AllShowsActivity
import com.android.tvmaze.shows.ShowsModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun bindHomeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ShowsModule::class])
    abstract fun bindAllShowsActivity(): AllShowsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FavoriteShowsModule::class])
    abstract fun bindFavoriteShowsActivity(): FavoriteShowsActivity
}
