package com.android.ashwiask.tvmaze.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsRepository
import com.android.ashwiask.tvmaze.network.TvMazeApi
import com.android.ashwiask.tvmaze.network.home.Episode
import com.android.ashwiask.tvmaze.network.home.Show
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeViewModel @Inject constructor(
    private val tvMazeApi: TvMazeApi,
    private val favoriteShowsRepository: FavoriteShowsRepository
) : ViewModel() {
    private val isLoading = MutableLiveData<Boolean>()
    private val homeData: MutableLiveData<HomeViewData> = MutableLiveData()
    private val errorMsg: MutableLiveData<String> = MutableLiveData()
    private lateinit var episodeViewDataList: MutableList<HomeViewData.EpisodeViewData>

    val country: String
        get() = COUNTRY_US

    fun onScreenCreated() {
        isLoading.value = true
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            val favoriteShowIds = withContext(Dispatchers.IO) {
                favoriteShowsRepository.allFavoriteShowIds()
            }
            val favoriteShowsWithFavorites = withContext(Dispatchers.IO) {
                val episodes = tvMazeApi.getCurrentSchedule(COUNTRY_US, currentDate)
                getShowsWithFavorites(episodes, favoriteShowIds)
            }
            withContext(Dispatchers.Main) {
                onSuccess(favoriteShowsWithFavorites)
            }
        }
    }

    private fun getShowsWithFavorites(
        episodes: List<Episode>,
        favoriteShowIds: List<Long>
    ): List<HomeViewData.EpisodeViewData> {
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
        Timber.e(throwable)
    }

    private fun onSuccess(favoriteShowsWithFavorites: List<HomeViewData.EpisodeViewData>) {
        isLoading.value = false
        homeData.value = HomeViewData(favoriteShowsWithFavorites)
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
