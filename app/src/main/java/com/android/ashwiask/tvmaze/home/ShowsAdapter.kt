package com.android.ashwiask.tvmaze.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.databinding.ShowListItemBinding
import com.android.ashwiask.tvmaze.network.home.Show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

const val IS_FAVORITE = "IS_FAVORITE"

class ShowsAdapter constructor(
    private val callback: Callback
) : RecyclerView.Adapter<ShowsAdapter.ShowHolder>() {
    private var episodeViewDataList: MutableList<HomeViewData.EpisodeViewData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val showListItemBinding = ShowListItemBinding
            .inflate(layoutInflater, parent, false)
        val showHolder = ShowHolder(showListItemBinding)
        showHolder.binding.showFavoriteIcon = true
        showHolder.binding.favorite.setOnClickListener { onFavouriteIconClicked(showHolder.adapterPosition) }
        return showHolder
    }

    fun updateList(newList: MutableList<HomeViewData.EpisodeViewData>) {
        val showDiffUtilCallback = ShowDiffUtilCallback(episodeViewDataList, newList)
        val diffResult = DiffUtil.calculateDiff(showDiffUtilCallback)
        episodeViewDataList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private fun onFavouriteIconClicked(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val episodeViewData = episodeViewDataList[position]
            val isFavorite = episodeViewData.showViewData.isFavoriteShow
            val updatedShowViewData = episodeViewData.showViewData.copy(isFavoriteShow = !isFavorite)
            val updatedEpisodeViewData = episodeViewData.copy(showViewData = updatedShowViewData)
            episodeViewDataList[position] = updatedEpisodeViewData
            notifyItemChanged(position)
            callback.onFavoriteClicked(episodeViewData.showViewData)
        }
    }

    override fun onBindViewHolder(holder: ShowHolder, position: Int) {
        val episodeViewData = episodeViewDataList[position]
        configureImage(holder.binding.showImage, episodeViewData.showViewData.show)
        configureFavoriteIcon(holder.binding.favorite, episodeViewData.showViewData.isFavoriteShow)
    }

    override fun onBindViewHolder(
        holder: ShowHolder, position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            val isFavorite = bundle.getBoolean(IS_FAVORITE)
            configureFavoriteIcon(holder.binding.favorite, isFavorite)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    private fun configureFavoriteIcon(favoriteIcon: ImageView, favorite: Boolean) {
        if (favorite) {
            val favoriteDrawable = AppCompatResources
                .getDrawable(favoriteIcon.context, R.drawable.favorite)
            favoriteIcon.setImageDrawable(favoriteDrawable)
        } else {
            val unFavoriteDrawable = AppCompatResources
                .getDrawable(favoriteIcon.context, R.drawable.favorite_border)
            favoriteIcon.setImageDrawable(unFavoriteDrawable)
        }
    }

    private fun configureImage(showImage: ImageView, show: Show) {
        if (show.image != null) {
            Glide.with(showImage.context).load(show.image[ORIGINAL_IMAGE])
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(showImage)
        }
    }

    override fun getItemCount(): Int {
        return episodeViewDataList.size
    }

    class ShowHolder(val binding: ShowListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface Callback {
        fun onFavoriteClicked(showViewData: HomeViewData.ShowViewData)
    }

    companion object {
        private const val ORIGINAL_IMAGE = "original"
    }
}
