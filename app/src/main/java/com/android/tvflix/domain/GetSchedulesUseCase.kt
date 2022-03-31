package com.android.tvflix.domain

import com.android.tvflix.di.IoDispatcher
import com.android.tvflix.model.respositores.SchedulesRepository
import com.android.tvflix.network.home.Episode
import kotlinx.coroutines.CoroutineDispatcher
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class GetSchedulesUseCase
@Inject
constructor(
    private val schedulesRepository: SchedulesRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) :
    SuspendUseCase<Unit, List<Episode>>(ioDispatcher) {
    override suspend fun execute(parameters: Unit): List<Episode> {
        return schedulesRepository.getSchedule(country = COUNTRY, currentDate = currentDate)
    }

    companion object {
        private const val QUERY_DATE_FORMAT = "yyyy-MM-dd"
        const val COUNTRY = "US"
        private val currentDate: String
            get() {
                val simpleDateFormat = SimpleDateFormat(QUERY_DATE_FORMAT, Locale.US)
                val calendar = Calendar.getInstance()
                return simpleDateFormat.format(calendar.time)
            }
    }

}