package com.android.ashwiask.tvmaze.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ashwiask.tvmaze.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ashwini Kumar.
 */

public class EpisodesScheduleAdapter extends RecyclerView.Adapter<EpisodesScheduleAdapter.EpisodeScheduleHolder>
{
    private List<Episode> episodes;

    public EpisodesScheduleAdapter(List<Episode> episodes)
    {
        this.episodes = episodes;
    }

    @Override
    public EpisodeScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item, parent, false);
        return new EpisodeScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeScheduleHolder holder, int position)
    {
        Episode episode = episodes.get(position);
        Show show = episode.getShow();
        holder.showTitle.setText(show.getName());
        holder.episodeTitle.setText(episode.getName());
        holder.episodeTime.setText(episode.getAirtime());
        holder.networkName.setText(show.getAirChannel().getName());
    }

    @Override
    public int getItemCount()
    {
        return episodes.size();
    }

    public static class EpisodeScheduleHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.show_name)
        TextView showTitle;
        @BindView(R.id.episode_name)
        TextView episodeTitle;
        @BindView(R.id.time)
        TextView episodeTime;
        @BindView(R.id.network_name)
        TextView networkName;

        public EpisodeScheduleHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
