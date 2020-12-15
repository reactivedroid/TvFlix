package com.android.tvmaze.favorite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tvmaze.db.favouriteshow.FavoriteShow
import com.android.tvmaze.di.CoroutinesDispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class FavoriteShowsViewModel @ViewModelInject
constructor(
    private val favoriteShowsRepository: FavoriteShowsRepository,
    // Inject coroutineDispatcher to facilitate Unit Testing
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {
    private val _favoriteShowsStateFlow =
        MutableStateFlow<FavoriteShowState>(FavoriteShowState.Loading)

    // Represents _favoriteShowsStateFlow mutable state flow as a read-only state flow.
    val favoriteShowsStateFlow = _favoriteShowsStateFlow.asStateFlow()

    private lateinit var removedFromFavoriteShows: List<FavoriteShow>

    fun loadFavoriteShows() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            onError(exception)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val favoriteShows =
                withContext(coroutinesDispatcherProvider.io) {
                    favoriteShowsRepository.allFavoriteShows()
                }
            withContext(coroutinesDispatcherProvider.main) {
                removedFromFavoriteShows = ArrayList(favoriteShows.size)
                if (favoriteShows.isNotEmpty()) {
                    _favoriteShowsStateFlow.emit(FavoriteShowState.AllFavorites(favoriteShows))
                } else {
                    _favoriteShowsStateFlow.emit(FavoriteShowState.Empty)
                }
            }
        }
    }

    private fun onError(throwable: Throwable) {
        _favoriteShowsStateFlow.value = FavoriteShowState.Error(throwable.localizedMessage)
        Timber.d(throwable)
    }

    fun onFavoriteClick(show: FavoriteShow) {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            if (!show.isFavorite) {
                favoriteShowsRepository.insertIntoFavorites(show)
                _favoriteShowsStateFlow.emit(FavoriteShowState.AddedToFavorites(show))
            } else {
                favoriteShowsRepository.removeFromFavorites(show)
                _favoriteShowsStateFlow.emit(FavoriteShowState.RemovedFromFavorites(show))
            }
        }
    }
}
