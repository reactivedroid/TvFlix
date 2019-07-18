package com.android.ashwiask.tvmaze.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsViewModel
import com.android.ashwiask.tvmaze.home.HomeViewModel
import com.android.ashwiask.tvmaze.shows.ShowsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowsViewModel::class)
    abstract fun bindShowsViewModel(showsViewModel: ShowsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteShowsViewModel::class)
    abstract fun bindFavoriteShowsViewModel(favoriteShowsViewModel: FavoriteShowsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: TvMazeViewModelFactory): ViewModelProvider.Factory
}
