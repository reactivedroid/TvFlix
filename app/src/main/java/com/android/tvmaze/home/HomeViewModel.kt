package com.android.tvmaze.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tvmaze.di.CoroutinesDispatcherProvider
import com.android.tvmaze.favorite.FavoriteShowsRepository
import com.android.tvmaze.network.TvMazeApi
import com.android.tvmaze.network.home.Episode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel @ViewModelInject constructor(
    private val tvMazeApi: TvMazeApi,
    private val favoriteShowsRepository: FavoriteShowsRepository,
    // Inject coroutineDispatcher to facilitate Unit Testing
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {
    private val _homeViewStateFlow = MutableStateFlow<HomeViewState>(HomeViewState.Loading)

    // Represents _homeViewStateFlow mutable state flow as a read-only state flow.
    val homeViewStateFlow = _homeViewStateFlow.asStateFlow()
    val country: String
        get() = COUNTRY_US

    fun onScreenCreated() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
        }
        viewModelScope.launch(coroutinesDispatcherProvider.io + coroutineExceptionHandler) {
            val favoriteShowIds = favoriteShowsRepository.allFavoriteShowIds()
            val episodes = tvMazeApi.getCurrentSchedule(country, currentDate)
            _homeViewStateFlow.emit(
                HomeViewState.Success(
                    HomeViewData(
                        getShowsWithFavorites(
                            episodes,
                            favoriteShowIds
                        )
                    )
                )
            )
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
        return episodeViewDataList
    }

    private fun onError(throwable: Throwable) {
        _homeViewStateFlow.value = HomeViewState.NetworkError(throwable.localizedMessage)
        Timber.e(throwable)
    }

    fun onFavoriteClick(showViewData: HomeViewData.ShowViewData) {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            if (!showViewData.isFavoriteShow) {
                favoriteShowsRepository.insertShowIntoFavorites(showViewData.show)
                _homeViewStateFlow.emit(HomeViewState.AddedToFavorites(showViewData.show))
            } else {
                favoriteShowsRepository.removeShowFromFavorites(showViewData.show)
                _homeViewStateFlow.emit(HomeViewState.RemovedFromFavorites(showViewData.show))
            }
        }
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
