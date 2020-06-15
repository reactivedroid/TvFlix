package com.android.tvmaze.shows

import androidx.lifecycle.ViewModel
import com.android.tvmaze.di.ViewModelFactoryBindingModule
import com.android.tvmaze.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryBindingModule::class])
abstract class ShowsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ShowsViewModel::class)
    abstract fun bindShowsViewModel(showsViewModel: ShowsViewModel): ViewModel
}