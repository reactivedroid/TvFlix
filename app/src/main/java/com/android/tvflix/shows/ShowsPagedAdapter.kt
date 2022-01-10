package com.android.tvflix.shows

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.tvflix.R
import com.android.tvflix.databinding.AllShowListItemBinding
import com.android.tvflix.network.home.Show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class ShowsPagedAdapter constructor(
    diffCallback: DiffUtil.ItemCallback<Show>
) : PagingDataAdapter<Show, RecyclerView.ViewHolder>(diffCallback) {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val showListItemBinding = AllShowListItemBinding.inflate(layoutInflater, parent, false)
        return ShowHolder(showListItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShowHolder) {
            val show = getItem(position)
            holder.binding.show = show
            if (show!!.rating != null) {
                holder.binding.rating = show.rating!!["average"]
            }
            configureImage(holder, show)
        }
    }

    private fun configureImage(holder: ShowHolder, show: Show) {
        if (show.image != null) {
            Glide.with(context).load(show.image["original"])
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.showImage)
        }
    }

    class ShowHolder(val binding: AllShowListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
