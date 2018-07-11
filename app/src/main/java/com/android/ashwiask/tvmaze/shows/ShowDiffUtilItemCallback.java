package com.android.ashwiask.tvmaze.shows;

import android.support.v7.util.DiffUtil;

import com.android.ashwiask.tvmaze.home.Show;

public class ShowDiffUtilItemCallback extends DiffUtil.ItemCallback<Show> {
    @Override
    public boolean areItemsTheSame(Show oldItem, Show newItem) {
        return oldItem.id() == newItem.id();
    }

    @Override
    public boolean areContentsTheSame(Show oldItem, Show newItem) {
        return oldItem.isFavorite() == newItem.isFavorite();
    }
}
