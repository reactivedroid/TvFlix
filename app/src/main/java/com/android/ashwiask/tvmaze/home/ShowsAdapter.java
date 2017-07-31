package com.android.ashwiask.tvmaze.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ashwiask.tvmaze.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ashwini Kumar.
 */

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ShowHolder>
{
    private static final String MEDIUM_IMAGE = "medium";
    private List<Episode> episodes;
    private Context context;

    public ShowsAdapter(List<Episode> episodes)
    {
        this.episodes = episodes;
    }

    @Override
    public ShowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.show_list_item, parent, false);
        ShowHolder showHolder = new ShowHolder(view);
        return showHolder;
    }

    @Override
    public void onBindViewHolder(ShowHolder holder, int position)
    {
        Episode episode = episodes.get(position);
        Show show = episode.getShow();
        holder.showTitle.setText(show.getName());
        holder.episodeTitle.setText(episode.getName());
        configureImage(holder, show);
    }

    private void configureImage(ShowHolder holder, Show show)
    {
        if (show.getImage() != null)
        {
            Glide.with(context).load(show.getImage().get(MEDIUM_IMAGE)).placeholder(R.color.grey).into(holder.showImage);
        } else
        {
            // do nothing
        }
    }

    @Override
    public int getItemCount()
    {
        return episodes.size();
    }

    public static class ShowHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.show_title)
        TextView showTitle;
        @BindView(R.id.episode_title)
        TextView episodeTitle;
        @BindView(R.id.show_image)
        ImageView showImage;

        ShowHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
