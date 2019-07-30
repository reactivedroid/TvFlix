package com.android.tvmaze.shows

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.android.tvmaze.network.home.Show

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowsDataSourceFactory @Inject
constructor(val showsDataSource: ShowsDataSource) : DataSource.Factory<Int, Show>() {
    private val showsDataSourceLiveData: MutableLiveData<ShowsDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, Show> {
        showsDataSourceLiveData.postValue(showsDataSource)
        return showsDataSource
    }
}
