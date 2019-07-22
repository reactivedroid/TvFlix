package com.android.ashwiask.tvmaze.home

sealed class HomeViewState
data class NetworkError(val message: String?) : HomeViewState()
object Loading : HomeViewState()
data class Success(val homeViewData: HomeViewData) : HomeViewState()