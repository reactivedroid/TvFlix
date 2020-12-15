package com.android.tvmaze.favorite

import com.android.tvmaze.db.favouriteshow.FavoriteShow

sealed class FavoriteShowState {
    data class AllFavorites(val favoriteShows: List<FavoriteShow>) : FavoriteShowState()
    data class Error(val message: String) : FavoriteShowState()
    object Loading : FavoriteShowState()
    object Empty : FavoriteShowState()
    data class AddedToFavorites(val show: FavoriteShow) : FavoriteShowState()
    data class RemovedFromFavorites(val show: FavoriteShow) : FavoriteShowState()
}
