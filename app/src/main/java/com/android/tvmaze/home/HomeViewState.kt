package com.android.tvmaze.home

import com.android.tvmaze.network.home.Show

sealed class HomeViewState {
    data class NetworkError(val message: String?) : HomeViewState()
    object Loading : HomeViewState()
    data class Success(val homeViewData: HomeViewData) : HomeViewState()
    data class AddedToFavorites(val show: Show) : HomeViewState()
    data class RemovedFromFavorites(val show: Show) : HomeViewState()
}