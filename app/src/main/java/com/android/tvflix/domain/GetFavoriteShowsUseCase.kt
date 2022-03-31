package com.android.tvflix.domain

import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.di.IoDispatcher
import com.android.tvflix.favorite.FavoriteShowsRepository
import com.android.tvflix.home.HomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetFavoriteShowsUseCase
@Inject
constructor(
    private val favoriteShowsRepository: FavoriteShowsRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : SuspendUseCase<Unit, List<Long>>(ioDispatcher) {
    override suspend fun execute(parameters: Unit): List<Long> {
        return favoriteShowsRepository.allFavoriteShowIds()
    }
}