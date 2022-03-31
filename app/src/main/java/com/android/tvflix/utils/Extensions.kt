package com.android.tvflix.utils

import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.network.home.Show

fun Show.toFavoriteShow(): FavoriteShow {
    return FavoriteShow(
        id = id,
        name = name,
        premiered = premiered,
        imageUrl = image!!["original"],
        summary = summary,
        rating = rating!!["average"],
        runtime = runtime!!
    )
}