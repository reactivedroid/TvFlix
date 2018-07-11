package com.android.ashwiask.tvmaze.favorite;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.databinding.ShowListItemBinding;
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FavoriteShowsAdapter extends RecyclerView.Adapter<FavoriteShowsAdapter.FavoriteShowHolder> {
    private final List<FavoriteShow> favoriteShows;
    private final Callback callback;

    public FavoriteShowsAdapter(List<FavoriteShow> favoriteShows,
                                Callback callback) {
        this.favoriteShows = favoriteShows;
        this.callback = callback;
    }

    @NonNull
    @Override
    public FavoriteShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ShowListItemBinding showListItemBinding =
                ShowListItemBinding.inflate(layoutInflater, parent, false);
        FavoriteShowHolder holder = new FavoriteShowHolder(showListItemBinding);
        holder.binding.favorite.setOnClickListener(v ->
                onFavouriteIconClicked(holder.getAdapterPosition()));
        return holder;
    }

    private void onFavouriteIconClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            FavoriteShow show = favoriteShows.get(position);
            show.setFavorite(!show.isFavorite());
            favoriteShows.set(position, show);
            notifyItemChanged(position);
            callback.onFavoriteClicked(show);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteShowHolder holder, int position) {
        FavoriteShow favoriteShow = favoriteShows.get(position);
        Glide.with(holder.itemView.getContext()).load(favoriteShow.getImageUrl())
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.showImage);
        configureFavoriteIcon(holder.binding.favorite, favoriteShow.isFavorite());
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

    @Override
    public int getItemCount() {
        return favoriteShows.size();
    }

    public static class FavoriteShowHolder extends RecyclerView.ViewHolder {
        private final ShowListItemBinding binding;

        public FavoriteShowHolder(ShowListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface Callback {
        void onFavoriteClicked(FavoriteShow show);
    }
}
