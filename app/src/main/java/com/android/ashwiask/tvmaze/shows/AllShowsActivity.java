package com.android.ashwiask.tvmaze.shows;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.base.TvMazeBaseActivity;
import com.android.ashwiask.tvmaze.common.Constants;
import com.android.ashwiask.tvmaze.common.GridItemDecoration;
import com.android.ashwiask.tvmaze.databinding.ActivityAllShowsBinding;
import com.android.ashwiask.tvmaze.home.Show;

import javax.inject.Inject;

public class AllShowsActivity extends TvMazeBaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ShowsViewModel showsViewModel;
    private ActivityAllShowsBinding binding;
    private ShowsPagedAdaptor showsPagedAdaptor;

    public static void start(Context context) {
        Intent starter = new Intent(context, AllShowsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_shows);
        setToolbar();
        showsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShowsViewModel.class);
        showsViewModel.onScreenCreated();
        initAdapter();
        showsViewModel.initialLoadState().observe(this, this::setProgress);
    }

    private void initAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NO_OF_COLUMNS);
        showsPagedAdaptor = new ShowsPagedAdaptor(new ShowsDiffUtilCallback());
        int spacing = getResources().getDimensionPixelSize(R.dimen.show_grid_spacing);
        binding.shows.addItemDecoration(new GridItemDecoration(spacing, Constants.NO_OF_COLUMNS));
        binding.shows.setLayoutManager(layoutManager);
        binding.shows.setAdapter(showsPagedAdaptor);
        showsViewModel.getShows().observe(this, this::showAllShows);
        showsViewModel.paginatedLoadState().observe(this, this::setAdapterState);
    }

    private void setAdapterState(NetworkState networkState) {
        showsPagedAdaptor.setNetworkState(networkState);
    }

    private void setToolbar() {
        Toolbar toolbar = binding.toolbar.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.shows);
    }

    private void setProgress(NetworkState initialLoadState) {
        switch (initialLoadState.status()) {
            case NetworkState.Status.SUCCESS:
                binding.progress.setVisibility(View.GONE);
                break;
            case NetworkState.Status.ERROR:
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(this, initialLoadState.message(), Toast.LENGTH_SHORT).show();
                break;
            case NetworkState.Status.LOADING:
            default:
                binding.progress.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showAllShows(PagedList<Show> shows) {
        showsPagedAdaptor.submitList(shows);
        binding.shows.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
