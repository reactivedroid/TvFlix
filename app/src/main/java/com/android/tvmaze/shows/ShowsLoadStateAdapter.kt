package com.android.tvmaze.shows

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ShowsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ShowsStateViewHolder>() {
    override fun onBindViewHolder(holder: ShowsStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ShowsStateViewHolder {
        return ShowsStateViewHolder.create(parent, retry)
    }
}