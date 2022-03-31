package com.android.tvflix.domain

import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.di.IoDispatcher
import com.android.tvflix.favorite.FavoriteShowsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddToFavoritesUseCase @Inject
constructor(
    private val favoriteShowsRepository: FavoriteShowsRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : SuspendUseCase<FavoriteShow, Unit>(ioDispatcher) {
    override suspend fun execute(parameters: FavoriteShow) {
        favoriteShowsRepository.insertIntoFavorites(parameters)
    }
}