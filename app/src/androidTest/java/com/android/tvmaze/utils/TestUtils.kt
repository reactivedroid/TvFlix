package com.android.tvmaze.utils

import com.android.tvmaze.db.favouriteshow.FavoriteShow

object TestUtils {
    fun getFakeShow(): FavoriteShow {
        return FavoriteShow(
            id = 222, name = "Friends",
            premiered = "Aug 2002", imageUrl = null,
            summary = "Friends for life!", rating = "10 stars",
            runtime = 132000, isFavorite = true
        )
    }
}