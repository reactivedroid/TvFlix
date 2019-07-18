package com.android.ashwiask.tvmaze.home

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class ShowDiffUtilCallback(
    private val oldList: List<HomeViewData.EpisodeViewData>,
    private val newList: List<HomeViewData.EpisodeViewData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEpisodeViewData = oldList[oldItemPosition]
        val newEpisodeViewData = newList[newItemPosition]
        return oldEpisodeViewData.showViewData.show.id == newEpisodeViewData.showViewData.show.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEpisodeViewData = oldList[oldItemPosition]
        val newEpisodeViewData = newList[newItemPosition]
        return oldEpisodeViewData.showViewData.isFavoriteShow == newEpisodeViewData.showViewData.isFavoriteShow
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldEpisodeViewData = oldList[oldItemPosition]
        val newEpisodeViewData = newList[newItemPosition]
        val bundle = Bundle()
        if (oldEpisodeViewData.showViewData.isFavoriteShow != newEpisodeViewData.showViewData.isFavoriteShow) {
            bundle.putBoolean(IS_FAVORITE, newEpisodeViewData.showViewData.isFavoriteShow)
        }
        return bundle
    }
}
