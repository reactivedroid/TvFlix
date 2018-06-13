package com.android.ashwiask.tvmaze.shows;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.android.ashwiask.tvmaze.home.Show;

import javax.inject.Inject;

public class ShowsViewModel extends ViewModel {
    private final ShowsDataSourceFactory showsDataSourceFactory;
    private LiveData<PagedList<Show>> shows;

    @Inject
    public ShowsViewModel(ShowsDataSourceFactory showsDataSourceFactory) {
        this.showsDataSourceFactory = showsDataSourceFactory;
    }

    public void onScreenCreated() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(40)
                .build();
        shows = new LivePagedListBuilder<>(showsDataSourceFactory, config).build();
    }

    public LiveData<PagedList<Show>> getShows() {
        return shows;
    }

    public LiveData<NetworkState> initialLoadState() {
        return showsDataSourceFactory.getShowsDataSource().getInitialLoadStateLiveData();
    }

    public LiveData<NetworkState> paginatedLoadState() {
        return showsDataSourceFactory.getShowsDataSource().getPaginatedNetworkStateLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        showsDataSourceFactory.getShowsDataSource().clear();
    }

    public void retry() {
        showsDataSourceFactory.getShowsDataSource().retryPagination();
    }
}
