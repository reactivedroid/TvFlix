package com.android.ashwiask.tvmaze.favorite

import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow
import com.android.ashwiask.tvmaze.db.favouriteshow.ShowDao
import com.android.ashwiask.tvmaze.network.home.Show
import io.reactivex.Single

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteShowsRepository @Inject
constructor(private val showDao: ShowDao) {

    val allFavoriteShows: Single<List<FavoriteShow>>
        get() = showDao.allFavouriteShows()

    fun insertShowIntoFavorites(show: Show) {
        val favoriteShow = FavoriteShow(
            id = show.id,
            name = show.name,
            premiered = show.premiered,
            imageUrl = show.image!!["original"],
            summary = show.summary,
            rating = show.rating!!["average"],
            runtime = show.runtime!!,
            isFavorite = true
        )
        showDao.insert(favoriteShow)
    }

    fun removeShowFromFavorites(show: Show) {
        val favoriteShow = FavoriteShow(
            id = show.id,
            name = show.name,
            premiered = show.premiered,
            imageUrl = show.image!!["original"],
            summary = show.summary,
            rating = show.rating!!["average"],
            runtime = show.runtime!!,
            isFavorite = false
        )
        showDao.remove(favoriteShow)
    }

    fun insertIntoFavorites(favoriteShow: FavoriteShow) {
        showDao.insert(favoriteShow)
    }

    fun removeFromFavorites(favoriteShow: FavoriteShow) {
        showDao.remove(favoriteShow)
    }

    val allFavoriteShowIds: Single<List<Long>>
        get() = showDao.getFavoriteShowIds()
}
