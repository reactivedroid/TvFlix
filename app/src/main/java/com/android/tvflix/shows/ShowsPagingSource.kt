package com.android.tvflix.shows

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.tvflix.network.TvFlixApi
import com.android.tvflix.network.home.Show
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ShowsPagingSource
@Inject
constructor(private val tvFlixApi: TvFlixApi) : PagingSource<Int, Show>() {
    companion object {
        const val SHOWS_STARTING_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        val position = params.key ?: SHOWS_STARTING_INDEX
        return try {
            val showsList = tvFlixApi.getShows(position)
            LoadResult.Page(
                data = showsList,
                prevKey = if (position == SHOWS_STARTING_INDEX) null else position - 1,
                nextKey = if (showsList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}