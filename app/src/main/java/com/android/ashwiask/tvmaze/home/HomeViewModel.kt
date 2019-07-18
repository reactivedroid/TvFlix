package com.android.ashwiask.tvmaze.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsRepository
import com.android.ashwiask.tvmaze.network.TvMazeApi
import com.android.ashwiask.tvmaze.network.home.Episode
import com.android.ashwiask.tvmaze.network.home.Show
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author Ashwini Kumar.
 */

class HomeViewModel @Inject constructor(
    private val tvMazeApi: TvMazeApi,
    private val favoriteShowsRepository: FavoriteShowsRepository
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val isLoading = MutableLiveData<Boolean>()
    private val homeData: MutableLiveData<HomeViewData> = MutableLiveData()
    private val errorMsg: MutableLiveData<String> = MutableLiveData()
    private lateinit var episodeViewDataList: MutableList<HomeViewData.EpisodeViewData>
    private var previousFavoriteList: List<Long>? = null

    val country: String
        get() = COUNTRY_US

    fun onScreenCreated() {
        isLoading.value = true
        val currentDate = currentDate
        val favoriteShows = favoriteShowsRepository.allFavoriteShowIds
        val episodes = tvMazeApi.getCurrentSchedule(COUNTRY_US, currentDate)
        val zippedSingleSource = Single.zip(
            episodes,
            favoriteShows,
            BiFunction(this@HomeViewModel::favorites)
        )
        val homeDisposable = zippedSingleSource
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { HomeViewData(it) }
            .subscribe(this::onSuccess, this::onError)
        compositeDisposable.add(homeDisposable)
    }

    private fun favorites(episodes: List<Episode>, favoriteShowIds: List<Long>): List<HomeViewData.EpisodeViewData> {
        previousFavoriteList = favoriteShowIds
        val episodeViewDataList = ArrayList<HomeViewData.EpisodeViewData>(episodes.size)
        for (episode in episodes) {
            val show = episode.show
            val showViewData = if (favoriteShowIds.contains(show.id)) {
                HomeViewData.ShowViewData(show, true)
            } else {
                HomeViewData.ShowViewData(show, false)
            }
            val episodeViewData = HomeViewData.EpisodeViewData(
                id = episode.id,
                showViewData = showViewData, url = episode.url, name = episode.name,
                season = episode.season, number = episode.number, airdate = episode.airdate,
                airtime = episode.airtime, runtime = episode.runtime
            )
            episodeViewDataList.add(episodeViewData)
        }
        this.episodeViewDataList = episodeViewDataList.toMutableList()
        return episodeViewDataList
    }

    private fun onError(throwable: Throwable) {
        isLoading.value = false
        errorMsg.value = throwable.message
    }

    private fun onSuccess(homeViewData: HomeViewData) {
        isLoading.value = false
        homeData.value = homeViewData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getPopularShowsList(): LiveData<HomeViewData> {
        return homeData
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun addToFavorite(show: Show) {
        favoriteShowsRepository.insertShowIntoFavorites(show)
    }

    fun removeFromFavorite(show: Show) {
        favoriteShowsRepository.removeShowFromFavorites(show)
    }

    companion object {
        private const val COUNTRY_US = "US"
        private const val QUERY_DATE_FORMAT = "yyyy-MM-dd"

        private val currentDate: String
            get() {
                val simpleDateFormat = SimpleDateFormat(QUERY_DATE_FORMAT, Locale.US)
                val calendar = Calendar.getInstance()
                return simpleDateFormat.format(calendar.time)
            }
    }
}
