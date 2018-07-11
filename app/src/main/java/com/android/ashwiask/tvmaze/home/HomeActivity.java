package com.android.ashwiask.tvmaze.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsActivity;
import com.android.ashwiask.tvmaze.shows.AllShowsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends TvMazeBaseActivity
        implements ShowsAdapter.Callback {
    private static final int NO_OF_COLUMNS = 2;
    private static final int REQUEST_CODE_FAVORITE_SHOWS = 11;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ShowsAdapter showsAdapter;

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

    private void showPopularShows(List<Show> shows) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, NO_OF_COLUMNS);
        layoutManager.setAutoMeasureEnabled(true);
        binding.popularShows.setLayoutManager(layoutManager);
        binding.popularShows.setHasFixedSize(true);
        showsAdapter = new ShowsAdapter(this);
        showsAdapter.updateList(shows);
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
        switch (item.getItemId()) {
            case R.id.action_shows:
                AllShowsActivity.start(this);
                return true;
            case R.id.action_favorites:
                FavoriteShowsActivity.startForResult(this, REQUEST_CODE_FAVORITE_SHOWS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFavoriteClicked(Show show) {
        if (show.isFavorite()) {
            homeViewModel.addToFavorite(show);
            Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
        } else {
            homeViewModel.removeFromFavorite(show);
            Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FAVORITE_SHOWS) {
            List<Show> removedFromFavoriteShows = data.getParcelableArrayListExtra(
                    FavoriteShowsActivity.EXTRAS_REMOVED_FROM_FAVORITE_SHOWS);
            if (!removedFromFavoriteShows.isEmpty()) {
                List<Show> showList = new ArrayList<>(showsAdapter.getShows());
                for (Show show : removedFromFavoriteShows) {
                    int index = showList.indexOf(show);
                    showList.set(index, show);
                }
                showsAdapter.updateList(showList);
            }
        }
    }
}
