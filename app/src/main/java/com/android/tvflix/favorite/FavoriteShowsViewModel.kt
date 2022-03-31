package com.android.tvflix.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FavoriteShowsViewModel @Inject
constructor(
    private val favoriteShowsRepository: FavoriteShowsRepository,
    // Inject coroutineDispatcher to facilitate Unit Testing
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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

        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            val favoriteShows = favoriteShowsRepository.allFavoriteShows()

            removedFromFavoriteShows = ArrayList(favoriteShows.size)
            if (favoriteShows.isNotEmpty()) {
                _favoriteShowsStateFlow.emit(FavoriteShowState.AllFavorites(favoriteShows))
            } else {
                _favoriteShowsStateFlow.emit(FavoriteShowState.Empty)
            }
        }
    }

    private fun onError(throwable: Throwable) {
        _favoriteShowsStateFlow.value =
            FavoriteShowState.Error(
                throwable.localizedMessage ?: "Some error occurred. Please try again."
            )
        Timber.d(throwable)
    }
}
