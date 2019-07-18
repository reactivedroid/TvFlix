package com.android.ashwiask.tvmaze.shows

sealed class NetworkState
data class NetworkError(val message: String?) : NetworkState()
object Loading : NetworkState()
object Success : NetworkState()