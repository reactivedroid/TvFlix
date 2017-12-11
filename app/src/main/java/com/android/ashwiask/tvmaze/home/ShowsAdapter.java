package com.android.ashwiask.tvmaze.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.databinding.ShowListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * @author Ashwini Kumar.
 */

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ShowHolder> {
    private static final String MEDIUM_IMAGE = "medium";
    private List<Episode> episodes;
    private Context context;

    public ShowsAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public ShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ShowListItemBinding showListItemBinding = ShowListItemBinding.inflate(layoutInflater, parent, false);
        return new ShowHolder(showListItemBinding);
    }

    @Override
    public void onBindViewHolder(ShowHolder holder, int position) {
        Episode episode = episodes.get(position);
        Show show = episode.getShow();
        holder.binding.setEpisode(episode);
        holder.binding.setShow(show);
        configureImage(holder, show);
    }

    private void configureImage(ShowHolder holder, Show show) {
        if (show.getImage() != null) {
            Glide.with(context).load(show.getImage().get(MEDIUM_IMAGE))
                    .apply(RequestOptions.placeholderOf(R.color.grey))
                    .into(holder.binding.showImage);
        } else {
            // do nothing
        }
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public static class ShowHolder extends RecyclerView.ViewHolder {
        private ShowListItemBinding binding;

        ShowHolder(ShowListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
