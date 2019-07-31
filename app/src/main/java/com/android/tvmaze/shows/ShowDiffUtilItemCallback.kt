package com.android.tvmaze.shows

import androidx.recyclerview.widget.DiffUtil
import com.android.tvmaze.network.home.Show

class ShowDiffUtilItemCallback : DiffUtil.ItemCallback<Show>() {
    override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
        return oldItem.name == newItem.name
    }
}
