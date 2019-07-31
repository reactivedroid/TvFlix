package com.android.tvmaze.favorite

import com.android.tvmaze.db.favouriteshow.FavoriteShow
import com.android.tvmaze.db.favouriteshow.ShowDao
import com.android.tvmaze.network.home.Show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteShowsRepository @Inject
constructor(private val showDao: ShowDao) {

    suspend fun allFavoriteShows(): List<FavoriteShow> {
        return showDao.allFavouriteShows()
    }

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
        CoroutineScope(Dispatchers.IO).launch { showDao.insert(favoriteShow) }
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
        CoroutineScope(Dispatchers.IO).launch { showDao.remove(favoriteShow) }
    }

    fun insertIntoFavorites(favoriteShow: FavoriteShow) {
        CoroutineScope(Dispatchers.IO).launch { showDao.insert(favoriteShow) }
    }

    fun removeFromFavorites(favoriteShow: FavoriteShow) {
        CoroutineScope(Dispatchers.IO).launch { showDao.remove(favoriteShow) }
    }

    suspend fun allFavoriteShowIds(): List<Long> {
        return showDao.getFavoriteShowIds()
    }

    fun clearAll() {
        CoroutineScope(Dispatchers.IO).launch { showDao.clear() }
    }
}
