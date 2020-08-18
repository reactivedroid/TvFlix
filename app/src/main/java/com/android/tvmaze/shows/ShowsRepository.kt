package com.android.tvmaze.shows

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.tvmaze.network.home.Show
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowsRepository
@Inject
constructor(private val showsPagingSource: ShowsPagingSource) {
    fun getShows(): Flow<PagingData<Show>> {
        return Pager(
            config = PagingConfig(
                pageSize = SHOWS_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { showsPagingSource }
        ).flow
    }

    companion object {
        const val SHOWS_PAGE_SIZE = 20
    }
}