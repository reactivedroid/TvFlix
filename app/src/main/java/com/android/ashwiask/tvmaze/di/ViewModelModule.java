package com.android.ashwiask.tvmaze.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.android.ashwiask.tvmaze.favorite.FavoriteShowsViewModel;
import com.android.ashwiask.tvmaze.home.HomeViewModel;
import com.android.ashwiask.tvmaze.shows.ShowsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShowsViewModel.class)
    abstract ViewModel bindShowsViewModel(ShowsViewModel showsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteShowsViewModel.class)
    abstract ViewModel bindFavoriteShowsViewModel(FavoriteShowsViewModel favoriteShowsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(TvMazeViewModelFactory factory);
}
