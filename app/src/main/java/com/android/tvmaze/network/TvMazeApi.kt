package com.android.tvmaze.network

import androidx.paging.PagingData
import com.android.tvmaze.network.home.Episode
import com.android.tvmaze.network.home.Show
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeApi {
    @GET("/schedule")
    suspend fun getCurrentSchedule(
        @Query("country") country: String,
        @Query("date") date: String
    ): List<Episode>

    @GET("/shows")
    suspend fun getShows(@Query("page") pageNumber: Int): List<Show>
}
