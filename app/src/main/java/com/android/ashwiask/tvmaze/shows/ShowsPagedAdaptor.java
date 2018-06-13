package com.android.ashwiask.tvmaze.shows;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.databinding.LoadingListItemBinding;
import com.android.ashwiask.tvmaze.databinding.NetworkFailureListItemBinding;
import com.android.ashwiask.tvmaze.databinding.ShowListItemBinding;
import com.android.ashwiask.tvmaze.home.Show;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class ShowsPagedAdaptor extends PagedListAdapter<Show, RecyclerView.ViewHolder> {
    private Context context;
    private NetworkState currentNetworkState;

    ShowsPagedAdaptor(@NonNull DiffUtil.ItemCallback<Show> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            case R.layout.loading_list_item:
                LoadingListItemBinding loadingListItemBinding =
                        LoadingListItemBinding.inflate(layoutInflater, parent, false);
                return new LoadingHolder(loadingListItemBinding);
            case R.layout.network_failure_list_item:
                NetworkFailureListItemBinding networkFailureListItemBinding =
                        NetworkFailureListItemBinding.inflate(layoutInflater, parent, false);
                return new NetworkFailureHolder(networkFailureListItemBinding);
            case R.layout.show_list_item:
            default:
                ShowListItemBinding showListItemBinding =
                        ShowListItemBinding.inflate(layoutInflater, parent, false);
                return new ShowHolder(showListItemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.loading_list_item:
                LoadingHolder loadingHolder = (LoadingHolder) holder;
                loadingHolder.binding.spinner.setVisibility(View.VISIBLE);
                break;
            case R.layout.network_failure_list_item:
                NetworkFailureHolder networkHolder = (NetworkFailureHolder) holder;
                networkHolder.binding.networkPbm.setText(currentNetworkState.message());
                break;
            default:
            case R.layout.show_list_item:
                Show show = getItem(position);
                configureImage((ShowHolder) holder, show);
                break;
        }
    }

    private void configureImage(ShowHolder holder, @NonNull Show show) {
        if (show.image() != null) {
            Glide.with(context).load(show.image().get("original"))
                    .apply(RequestOptions.placeholderOf(R.color.grey))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.binding.showImage);
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousNetworkState = currentNetworkState;
        boolean hadExtraRow = hasExtraRow();
        currentNetworkState = newNetworkState;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && !previousNetworkState.equals(newNetworkState)) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + getExtraRow();
    }

    // Add an item for the network state
    private int getExtraRow() {
        if (hasExtraRow()) return 1;
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // Reached at the end
        if (hasExtraRow() && position == getItemCount() - 1) {
            if (currentNetworkState.status() == NetworkState.Status.LOADING) {
                return R.layout.loading_list_item;
            } else {
                return R.layout.network_failure_list_item;
            }
        } else {
            return R.layout.show_list_item;
        }
    }

    private boolean hasExtraRow() {
        return currentNetworkState != null && currentNetworkState.status() != NetworkState.Status.SUCCESS;
    }

    static class ShowHolder extends RecyclerView.ViewHolder {
        private ShowListItemBinding binding;

        ShowHolder(ShowListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    static class LoadingHolder extends RecyclerView.ViewHolder {
        private LoadingListItemBinding binding;

        LoadingHolder(LoadingListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class NetworkFailureHolder extends RecyclerView.ViewHolder {
        private NetworkFailureListItemBinding binding;

        NetworkFailureHolder(NetworkFailureListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
