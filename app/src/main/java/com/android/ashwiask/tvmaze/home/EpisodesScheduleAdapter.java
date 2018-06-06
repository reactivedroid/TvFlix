package com.android.ashwiask.tvmaze.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.ashwiask.tvmaze.databinding.ScheduleListItemBinding;

import java.util.List;

/**
 * @author Ashwini Kumar.
 */

public class EpisodesScheduleAdapter extends
        RecyclerView.Adapter<EpisodesScheduleAdapter.EpisodeScheduleHolder> {
    private List<Episode> episodes;

    public EpisodesScheduleAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public EpisodeScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ScheduleListItemBinding itemBinding =
                ScheduleListItemBinding.inflate(layoutInflater, parent, false);
        return new EpisodeScheduleHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(EpisodeScheduleHolder holder, int position) {
        Episode episode = episodes.get(position);
        Show show = episode.show();
        holder.binding.setEpisode(episode);
        holder.binding.setShow(show);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpisodeScheduleHolder extends RecyclerView.ViewHolder {
        private ScheduleListItemBinding binding;

        EpisodeScheduleHolder(ScheduleListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
