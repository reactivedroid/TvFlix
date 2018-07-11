package com.android.ashwiask.tvmaze.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

public class ShowDiffUtilCallback extends DiffUtil.Callback {
    private final List<Show> oldList;
    private final List<Show> newList;

    public ShowDiffUtilCallback(List<Show> oldList, List<Show> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Show oldShow = oldList.get(oldItemPosition);
        Show newShow = newList.get(newItemPosition);
        return oldShow.id() == newShow.id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Show oldShow = oldList.get(oldItemPosition);
        Show newShow = newList.get(newItemPosition);
        return oldShow.isFavorite() == newShow.isFavorite();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Show oldShow = oldList.get(oldItemPosition);
        Show newShow = newList.get(newItemPosition);
        Bundle bundle = new Bundle();
        if (oldShow.isFavorite() != newShow.isFavorite()) {
            bundle.putBoolean("IS_FAVORITE", newShow.isFavorite());
        }
        return bundle;
    }
}
