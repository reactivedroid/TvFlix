package com.android.tvflix.utils

import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.home.HomeViewData
import com.android.tvflix.network.home.Episode
import com.android.tvflix.network.home.Show
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object TestUtil {
    fun getFakeEpisodeViewDataList(isFavorite: Boolean): List<HomeViewData.EpisodeViewData> {
        val episodes = getFakeEpisodeList()
        val episodeViewDataList = ArrayList<HomeViewData.EpisodeViewData>(episodes.size)
        episodes.forEach {
            val showViewData = HomeViewData.ShowViewData(it.show, isFavorite)
            val episodeViewData = HomeViewData.EpisodeViewData(
                id = it.id,
                showViewData = showViewData,
                url = it.url,
                name = it.name,
                season = it.season,
                number = it.number,
                airdate = it.airdate,
                airtime = it.airtime,
                runtime = it.runtime
            )
            episodeViewDataList.add(episodeViewData)
        }
        return episodeViewDataList
    }

    val currentDate: String
        get() {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val calendar = Calendar.getInstance()
            return simpleDateFormat.format(calendar.time)
        }

    fun getFakeEpisodeList(): List<Episode> {
        val episodeList = ArrayList<Episode>(2)
        val show1 = Show(
            id = 1, url = null, name = "Friends", type = "Show",
            language = "English", genres = emptyList(), status = "Ended", runtime = 13200,
            premiered = null, officialSite = null, airChannel = null, webChannel = null,
            image = null, externalInfo = null, summary = "Friends for life!", rating = null
        )
        val episode1 = Episode(
            show = show1, id = 101, url = null, name = "The One with the Reunion - Part 1",
            season = 11, number = 1, airdate = null, airtime = null, runtime = 13200
        )
        val show2 = Show(
            id = 2, url = null, name = "Breaking Bad", type = "Show",
            language = "English", genres = emptyList(), status = "Ended", runtime = 26000,
            premiered = null, officialSite = null, airChannel = null, webChannel = null,
            image = null, externalInfo = null, summary = "Crime Drama", rating = null
        )
        val episode2 = Episode(
            show = show2, id = 102, url = null, name = "I am the one who Knocks!",
            season = 11, number = 2, airdate = null, airtime = null, runtime = 13200
        )
        episodeList.add(episode1)
        episodeList.add(episode2)
        return episodeList
    }

    fun getFakeShow(): FavoriteShow {
        return FavoriteShow(
            id = 222, name = "Friends",
            premiered = "Aug 2002", imageUrl = null,
            summary = "Friends for life!", rating = "10 stars",
            runtime = 132000, isFavorite = true
        )
    }
}