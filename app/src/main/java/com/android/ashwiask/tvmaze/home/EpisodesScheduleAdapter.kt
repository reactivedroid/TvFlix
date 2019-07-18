package com.android.ashwiask.tvmaze.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.ashwiask.tvmaze.databinding.ScheduleListItemBinding
import com.android.ashwiask.tvmaze.network.home.Episode

/**
 * @author Ashwini Kumar.
 */

class EpisodesScheduleAdapter constructor(private val episodes: List<Episode>) :
    RecyclerView.Adapter<EpisodesScheduleAdapter.EpisodeScheduleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeScheduleHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ScheduleListItemBinding.inflate(layoutInflater, parent, false)
        return EpisodeScheduleHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: EpisodeScheduleHolder, position: Int) {
        val episode = episodes[position]
        val show = episode.show
        holder.binding.episode = episode
        holder.binding.show = show
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    class EpisodeScheduleHolder(val binding: ScheduleListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
