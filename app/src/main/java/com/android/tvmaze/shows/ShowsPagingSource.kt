package com.android.tvmaze.shows

import androidx.paging.PagingSource
import com.android.tvmaze.network.TvMazeApi
import com.android.tvmaze.network.home.Show
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ShowsPagingSource
@Inject
constructor(private val tvMazeApi: TvMazeApi) : PagingSource<Int, Show>() {
    companion object {
        const val SHOWS_STARTING_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        val position = params.key ?: SHOWS_STARTING_INDEX
        return try {
            val showsList = tvMazeApi.getShows(position)
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
}