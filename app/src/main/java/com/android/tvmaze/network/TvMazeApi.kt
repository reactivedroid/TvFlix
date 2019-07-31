package com.android.tvmaze.network

import com.android.tvmaze.network.home.Episode
import com.android.tvmaze.network.home.Show
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
