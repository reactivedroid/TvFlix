package com.android.tvmaze.favorite

import com.android.tvmaze.db.favouriteshow.FavoriteShow
import com.android.tvmaze.db.favouriteshow.ShowDao
import com.android.tvmaze.network.home.Show
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteShowsRepository @Inject
constructor(private val showDao: ShowDao) {

    suspend fun allFavoriteShows(): List<FavoriteShow> {
        return showDao.allFavouriteShows()
    }

    suspend fun insertShowIntoFavorites(show: Show) {
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

    suspend fun removeShowFromFavorites(show: Show) {
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

    suspend fun insertIntoFavorites(favoriteShow: FavoriteShow) {
        showDao.insert(favoriteShow)
    }

    suspend fun removeFromFavorites(favoriteShow: FavoriteShow) {
        showDao.remove(favoriteShow)
    }

    suspend fun allFavoriteShowIds(): List<Long> {
        return showDao.getFavoriteShowIds()
    }

    suspend fun clearAll() {
        showDao.clear()
    }
}
