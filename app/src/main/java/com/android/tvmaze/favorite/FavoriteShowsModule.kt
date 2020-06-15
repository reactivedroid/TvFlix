package com.android.tvmaze.favorite

import androidx.lifecycle.ViewModel
import com.android.tvmaze.di.ViewModelFactoryBindingModule
import com.android.tvmaze.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryBindingModule::class])
abstract class FavoriteShowsModule {
    @Binds
    @IntoMap
    @ViewModelKey(FavoriteShowsViewModel::class)
    abstract fun bindFavoriteShowsViewModel(favoriteShowsViewModel: FavoriteShowsViewModel): ViewModel
}