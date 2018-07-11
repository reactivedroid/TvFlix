package com.android.ashwiask.tvmaze.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.databinding.ShowListItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.List;

/**
 * @author Ashwini Kumar.
 */

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ShowHolder> {
    private static final String ORIGINAL_IMAGE = "original";
    private List<Show> shows;
    private final Callback callback;

    ShowsAdapter(Callback callback) {
        this.callback = callback;
        shows = Collections.emptyList();
    }

    public void updateList(List<Show> newList) {
        ShowDiffUtilCallback showDiffUtilCallback = new ShowDiffUtilCallback(shows, newList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(showDiffUtilCallback);
        shows = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ShowListItemBinding showListItemBinding = ShowListItemBinding
                .inflate(layoutInflater, parent, false);
        ShowHolder showHolder = new ShowHolder(showListItemBinding);
        showHolder.binding.favorite.setOnClickListener(v ->
                onFavouriteIconClicked(showHolder.getAdapterPosition()));
        return showHolder;
    }

    private void onFavouriteIconClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Show show = shows.get(position);
            show = show.toBuilder().isFavorite(!show.isFavorite())
                    .build();
            shows.set(position, show);
            notifyItemChanged(position);
            callback.onFavoriteClicked(show);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHolder holder, int position) {
        Show show = shows.get(position);
        configureImage(holder.binding.showImage, show);
        configureFavoriteIcon(holder.binding.favorite, show.isFavorite());
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHolder holder, int position,
                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            Bundle bundle = (Bundle) payloads.get(0);
            boolean isFavorite = bundle.getBoolean("IS_FAVORITE");
            configureFavoriteIcon(holder.binding.favorite, isFavorite);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    private void configureFavoriteIcon(ImageView favoriteIcon, boolean favorite) {
        if (favorite) {
            Drawable favoriteDrawable = AppCompatResources
                    .getDrawable(favoriteIcon.getContext(), R.drawable.favorite);
            favoriteIcon.setImageDrawable(favoriteDrawable);
        } else {
            Drawable unFavoriteDrawable = AppCompatResources
                    .getDrawable(favoriteIcon.getContext(), R.drawable.favorite_border);
            favoriteIcon.setImageDrawable(unFavoriteDrawable);
        }
    }

    private void configureImage(ImageView showImage, Show show) {
        if (show.image() != null) {
            Glide.with(showImage.getContext()).load(show.image().get(ORIGINAL_IMAGE))
                    .apply(RequestOptions.placeholderOf(R.color.grey))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(showImage);
        }
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public List<Show> getShows() {
        return shows;
    }

    static class ShowHolder extends RecyclerView.ViewHolder {
        private ShowListItemBinding binding;

        ShowHolder(ShowListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public interface Callback {
        void onFavoriteClicked(Show show);
    }
}
