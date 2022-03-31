package com.android.tvflix.model.respositores

import com.android.tvflix.network.TvFlixApi
import com.android.tvflix.network.home.Episode
import javax.inject.Inject

class SchedulesRepository
@Inject
constructor(private val tvFlixApi: TvFlixApi) {
    suspend fun getSchedule(country: String, currentDate: String): List<Episode> {
        return tvFlixApi.getCurrentSchedule(country, currentDate)
    }
}