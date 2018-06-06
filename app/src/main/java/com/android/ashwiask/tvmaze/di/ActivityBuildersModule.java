package com.android.ashwiask.tvmaze.di;

import com.android.ashwiask.tvmaze.home.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract HomeActivity bindHomeActivity();
}
