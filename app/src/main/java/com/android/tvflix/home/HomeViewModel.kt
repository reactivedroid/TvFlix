package com.android.tvflix.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tvflix.R
import com.android.tvflix.analytics.Analytics
import com.android.tvflix.analytics.Event
import com.android.tvflix.analytics.EventNames
import com.android.tvflix.analytics.EventParams
import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.di.IoDispatcher
import com.android.tvflix.domain.AddToFavoritesUseCase
import com.android.tvflix.domain.GetFavoriteShowsUseCase
import com.android.tvflix.domain.GetSchedulesUseCase
import com.android.tvflix.domain.RemoveFromFavoritesUseCase
import com.android.tvflix.network.home.Episode
import com.android.tvflix.utils.toFavoriteShow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val getFavoriteShowsUseCase: GetFavoriteShowsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    // Inject coroutineDispatcher to facilitate Unit Testing
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val analytics: Analytics
) : ViewModel() {
    private val _homeViewStateFlow = MutableStateFlow<HomeViewState>(HomeViewState.Loading)

    // Represents _homeViewStateFlow mutable state flow as a read-only state flow.
    val homeViewStateFlow = _homeViewStateFlow.asStateFlow()

    fun onScreenCreated() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
        }
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            val favoriteShowIds = getFavoriteShowsUseCase.invoke(Unit)
            val episodes = getSchedulesUseCase.invoke(Unit)
            _homeViewStateFlow.emit(
                HomeViewState.Success(
                    HomeViewData(
                        heading(),
                        getShowsWithFavorites(
                            episodes,
                            favoriteShowIds
                        )
                    )
                )
            )
        }
    }

    fun heading(): String {
        return String.format(
            context.getString(R.string.popular_shows_airing_today),
            GetSchedulesUseCase.COUNTRY
        )
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
        viewModelScope.launch(dispatcher) {
            if (!showViewData.isFavoriteShow) {
                analytics.sendEvent(
                    Event(
                        EventNames.CLICK,
                        hashMapOf(
                            EventParams.CLICK_CONTEXT to "favorite",
                            EventParams.CONTENT_ID to showViewData.show.id,
                            EventParams.CONTENT_TITLE to showViewData.show.name,
                            EventParams.IS_FAVORITE_MARKED to true
                        )
                    )
                )
                addToFavoritesUseCase.invoke(showViewData.show.toFavoriteShow())
                _homeViewStateFlow.emit(HomeViewState.AddedToFavorites(showViewData.show))
            } else {
                analytics.sendEvent(
                    Event(
                        EventNames.CLICK,
                        hashMapOf(
                            EventParams.CLICK_CONTEXT to "favorite",
                            EventParams.CONTENT_ID to showViewData.show.id,
                            EventParams.CONTENT_TITLE to showViewData.show.name,
                            EventParams.IS_FAVORITE_MARKED to false
                        )
                    )
                )
                removeFromFavoritesUseCase.invoke(showViewData.show.toFavoriteShow())
                _homeViewStateFlow.emit(HomeViewState.RemovedFromFavorites(showViewData.show))
            }
        }
    }
}
