package com.android.ashwiask.tvmaze.shows

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.ashwiask.tvmaze.R
import com.android.ashwiask.tvmaze.databinding.AllShowListItemBinding
import com.android.ashwiask.tvmaze.databinding.LoadingListItemBinding
import com.android.ashwiask.tvmaze.databinding.NetworkFailureListItemBinding
import com.android.ashwiask.tvmaze.network.home.Show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class ShowsPagedAdaptor constructor(
    diffCallback: DiffUtil.ItemCallback<Show>,
    private val callback: Callback
) : PagedListAdapter<Show, RecyclerView.ViewHolder>(diffCallback) {
    private lateinit var context: Context
    private var currentNetworkState: NetworkState = Loading

    // Add an item for the network state
    private val extraRow: Int
        get() = if (hasExtraRow())
            1
        else
            0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        when (viewType) {
            R.layout.loading_list_item -> {
                val loadingListItemBinding = LoadingListItemBinding.inflate(layoutInflater, parent, false)
                return LoadingHolder(loadingListItemBinding)
            }
            R.layout.network_failure_list_item -> {
                val networkFailureListItemBinding = NetworkFailureListItemBinding.inflate(layoutInflater, parent, false)
                val networkFailureHolder = NetworkFailureHolder(networkFailureListItemBinding)
                networkFailureHolder.binding.retry.setOnClickListener { callback.onRetryClicked() }
                return networkFailureHolder
            }
            else -> {
                val showListItemBinding = AllShowListItemBinding.inflate(layoutInflater, parent, false)
                return ShowHolder(showListItemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.loading_list_item -> {
                val loadingHolder = holder as LoadingHolder
                loadingHolder.binding.spinner.visibility = View.VISIBLE
            }
            R.layout.network_failure_list_item -> {
                val networkHolder = holder as NetworkFailureHolder
                val networkError = currentNetworkState as NetworkError
                networkHolder.binding.networkPbm.text = networkError.message
            }
            R.layout.show_list_item -> {
                val show = getItem(position)
                val showHolder = holder as ShowHolder
                showHolder.binding.show = show
                if (show!!.rating != null) {
                    showHolder.binding.rating = show.rating!!["average"]
                }
                configureImage(showHolder, show)
            }
        }
    }

    private fun configureImage(holder: ShowHolder, show: Show) {
        if (show.image != null) {
            Glide.with(context).load(show.image["original"])
                .apply(RequestOptions.placeholderOf(R.color.grey))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.showImage)
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousNetworkState = currentNetworkState
        val hadExtraRow = hasExtraRow()
        currentNetworkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousNetworkState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + extraRow
    }

    override fun getItemViewType(position: Int): Int {
        // Reached at the end
        return if (hasExtraRow() && position == itemCount - 1) {
            if (currentNetworkState === Loading) {
                R.layout.loading_list_item
            } else {
                R.layout.network_failure_list_item
            }
        } else {
            R.layout.show_list_item
        }
    }

    private fun hasExtraRow(): Boolean {
        return currentNetworkState == Success
    }

    class ShowHolder(val binding: AllShowListItemBinding) : RecyclerView.ViewHolder(binding.root)

    class LoadingHolder(val binding: LoadingListItemBinding) : RecyclerView.ViewHolder(binding.root)

    class NetworkFailureHolder(val binding: NetworkFailureListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface Callback {
        fun onRetryClicked()
    }
}
