package com.android.tvmaze.home

import androidx.lifecycle.ViewModel
import com.android.tvmaze.di.ViewModelFactoryBindingModule
import com.android.tvmaze.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryBindingModule::class])
abstract class HomeModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}