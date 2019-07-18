package com.android.ashwiask.tvmaze.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FavoriteShowsViewModel @Inject
constructor(private val favoriteShowsRepository: FavoriteShowsRepository) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val favoriteShowsLiveData: MutableLiveData<List<FavoriteShow>> = MutableLiveData()
    private lateinit var removedFromFavoriteShows: List<FavoriteShow>

    fun loadFavoriteShows() {
        val favoriteShowsDisposable = favoriteShowsRepository.allFavoriteShows
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onFavoritesFetched, this::onError)
        compositeDisposable.add(favoriteShowsDisposable)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun addToFavorite(show: FavoriteShow) {
        favoriteShowsRepository.insertIntoFavorites(show)
    }

    fun removeFromFavorite(show: FavoriteShow) {
        favoriteShowsRepository.removeFromFavorites(show)
    }
}
