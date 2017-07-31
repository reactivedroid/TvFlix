package com.android.ashwiask.tvmaze.home;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Ashwini Kumar.
 */

public interface HomeEndpoint
{
    @GET("/schedule")
    Single<List<Episode>> getCurrentSchedule(@Query("country") String country, @Query("date") String date);
}
