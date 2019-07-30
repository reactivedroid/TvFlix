package com.android.tvmaze.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.android.tvmaze.network.home.Show
import javax.inject.Inject

class ShowsViewModel @Inject
constructor(private val showsDataSourceFactory: ShowsDataSourceFactory) : ViewModel() {
    private lateinit var shows: LiveData<PagedList<Show>>

    fun onScreenCreated() {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(40)
            .build()
        shows = LivePagedListBuilder(showsDataSourceFactory, config).build()
    }

    fun getShows(): LiveData<PagedList<Show>> {
        return shows
    }

    fun initialLoadState(): LiveData<NetworkState> {
        return showsDataSourceFactory.showsDataSource.getInitialLoadStateLiveData()
    }

    fun paginatedLoadState(): LiveData<NetworkState> {
        return showsDataSourceFactory.showsDataSource.getPaginatedNetworkStateLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        showsDataSourceFactory.showsDataSource.clear()
    }

    fun retry() {
        showsDataSourceFactory.showsDataSource.retryPagination()
    }
}
