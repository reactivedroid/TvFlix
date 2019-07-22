package com.android.ashwiask.tvmaze.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FavoriteShowsViewModel @Inject
constructor(private val favoriteShowsRepository: FavoriteShowsRepository) : ViewModel() {
    private val favoriteShowsLiveData: MutableLiveData<List<FavoriteShow>> = MutableLiveData()
    private lateinit var removedFromFavoriteShows: List<FavoriteShow>

    fun loadFavoriteShows() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val favoriteShows = withContext(Dispatchers.IO + coroutineExceptionHandler) {
                favoriteShowsRepository.allFavoriteShows()
            }
            withContext(Dispatchers.Main + coroutineExceptionHandler) {
                onFavoritesFetched(favoriteShows)
            }
        }
    }

    private fun onError(throwable: Throwable) {
        Timber.d(throwable)
    }

    private fun onFavoritesFetched(favoriteShows: List<FavoriteShow>) {
        removedFromFavoriteShows = ArrayList(favoriteShows.size)
        favoriteShowsLiveData.value = favoriteShows
    }

    fun getFavoriteShowsLiveData(): LiveData<List<FavoriteShow>> {
        return favoriteShowsLiveData
    }

    fun addToFavorite(show: FavoriteShow) {
        favoriteShowsRepository.insertIntoFavorites(show)
    }

    fun removeFromFavorite(show: FavoriteShow) {
        favoriteShowsRepository.removeFromFavorites(show)
    }
}
