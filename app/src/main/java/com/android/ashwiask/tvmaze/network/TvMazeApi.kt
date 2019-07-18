package com.android.ashwiask.tvmaze.network

import com.android.ashwiask.tvmaze.network.home.Episode
import com.android.ashwiask.tvmaze.network.home.Show
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Ashwini Kumar.
 */

interface TvMazeApi {
    @GET("/schedule")
    fun getCurrentSchedule(
        @Query("country") country: String,
        @Query("date") date: String
    ): Single<List<Episode>>


    @GET("/shows")
    fun getShows(@Query("page") pageNumber: Int): Single<List<Show>>
}
