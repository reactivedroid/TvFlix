package com.android.tvflix.network

import com.android.tvflix.network.home.Episode
import com.android.tvflix.network.home.Show
import retrofit2.http.GET
import retrofit2.http.Query

interface TvFlixApi {
    @GET("/schedule")
    suspend fun getCurrentSchedule(
        @Query("country") country: String,
        @Query("date") date: String
    ): List<Episode>

    @GET("/shows")
    suspend fun getShows(@Query("page") pageNumber: Int): List<Show>
}
