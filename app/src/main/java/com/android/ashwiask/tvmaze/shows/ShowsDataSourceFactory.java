package com.android.ashwiask.tvmaze.shows;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.android.ashwiask.tvmaze.home.Show;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShowsDataSourceFactory extends DataSource.Factory<Integer, Show> {
    private final ShowsDataSource showsDataSource;
    private MutableLiveData<ShowsDataSource> showsDataSourceLiveData;

    @Inject
    public ShowsDataSourceFactory(ShowsDataSource showsDataSource) {
        this.showsDataSource = showsDataSource;
        showsDataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Show> create() {
        showsDataSourceLiveData.postValue(showsDataSource);
        return showsDataSource;
    }

    public ShowsDataSource getShowsDataSource() {
        return showsDataSource;
    }
}
