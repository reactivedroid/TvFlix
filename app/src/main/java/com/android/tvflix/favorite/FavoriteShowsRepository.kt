package com.android.tvflix.favorite

import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.db.favouriteshow.ShowDao
import com.android.tvflix.network.home.Show
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
            runtime = show.runtime!!
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
            runtime = show.runtime!!
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
