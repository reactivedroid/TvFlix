package com.android.tvmaze.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryBindingModule {
    @Binds
    abstract fun bindViewModelFactory(tvMazeViewModelFactory: TvMazeViewModelFactory): ViewModelProvider.Factory
}