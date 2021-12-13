package com.android.tvflix.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.android.tvflix.R
import com.android.tvflix.databinding.ShowListItemBinding
import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class FavoriteShowsAdapter(
    private val favoriteShows: MutableList<FavoriteShow>,
    private val callback: Callback
) : RecyclerView.Adapter<FavoriteShowsAdapter.FavoriteShowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteShowHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val showListItemBinding = ShowListItemBinding.inflate(layoutInflater, parent, false)
        val holder = FavoriteShowHolder(showListItemBinding)
        holder.binding.favorite.setOnClickListener { onFavouriteIconClicked(holder.absoluteAdapterPosition) }
        holder.binding.showFavoriteIcon = false
        return holder
    }

    private fun onFavouriteIconClicked(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val show = favoriteShows[position]
            val updatedShow = show.copy(isFavorite = !show.isFavorite)
            favoriteShows[position] = updatedShow
            notifyItemChanged(position)
            callback.onFavoriteClicked(show)
        }
    }

    override fun onBindViewHolder(holder: FavoriteShowHolder, position: Int) {
        val favoriteShow = favoriteShows[position]
        Glide.with(holder.itemView.context).load(favoriteShow.imageUrl)
            .apply(RequestOptions.placeholderOf(R.color.grey))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.showImage)
        configureFavoriteIcon(holder.binding.favorite, favoriteShow.isFavorite)
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

    override fun getItemCount(): Int {
        return favoriteShows.size
    }

    class FavoriteShowHolder(val binding: ShowListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface Callback {
        fun onFavoriteClicked(show: FavoriteShow)
    }
}
