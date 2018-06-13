package com.android.ashwiask.tvmaze.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.base.TvMazeBaseActivity;
import com.android.ashwiask.tvmaze.common.GridItemDecoration;
import com.android.ashwiask.tvmaze.databinding.ActivityHomeBinding;
import com.android.ashwiask.tvmaze.shows.AllShowsActivity;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends TvMazeBaseActivity {
    private static final int NO_OF_COLUMNS = 2;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        setToolbar();
        homeViewModel.onScreenCreated();
        homeViewModel.isLoading().observe(this, this::setProgress);
        homeViewModel.getScheduleList().observe(this, this::showSchedule);
        homeViewModel.getPopularShowsList().observe(this, this::showPopularShows);
        homeViewModel.getErrorMsg().observe(this, this::showError);
    }

    private void setToolbar() {
        Toolbar toolbar = binding.toolbar.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setTitle(R.string.app_name);
    }

    private void setProgress(boolean isLoading) {
        if (isLoading) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    private void showPopularShows(List<Episode> episodes) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, NO_OF_COLUMNS);
        layoutManager.setAutoMeasureEnabled(true);
        binding.popularShows.setLayoutManager(layoutManager);
        binding.popularShows.setHasFixedSize(true);
        ShowsAdapter showsAdapter = new ShowsAdapter(episodes);
        binding.popularShows.setAdapter(showsAdapter);
        int spacing = getResources().getDimensionPixelSize(R.dimen.show_grid_spacing);
        binding.popularShows.addItemDecoration(new GridItemDecoration(spacing, NO_OF_COLUMNS));
        binding.popularShows.setNestedScrollingEnabled(false);
        binding.popularShowHeader.setText(String.format(getString(R.string.popular_shows_airing_today),
                homeViewModel.getCountry()));
        binding.homeContainer.setVisibility(View.VISIBLE);
    }

    private void showSchedule(List<Episode> episodes) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setAutoMeasureEnabled(true);
        binding.scheduleList.setLayoutManager(layoutManager);
        binding.scheduleList.setHasFixedSize(true);
        EpisodesScheduleAdapter episodesScheduleAdapter = new EpisodesScheduleAdapter(episodes);
        binding.scheduleList.setAdapter(episodesScheduleAdapter);
        binding.scheduleList.setNestedScrollingEnabled(false);
        binding.schedule.setText(String.format(getString(R.string.schedule_for), homeViewModel.getScheduleDate()));
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showProgress() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_shows) {
            AllShowsActivity.start(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
