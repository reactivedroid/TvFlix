package com.android.ashwiask.tvmaze.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.TvMazeApplication;
import com.android.ashwiask.tvmaze.common.GridItemDecoration;
import com.android.ashwiask.tvmaze.databinding.ActivityHomeBinding;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity implements HomeView
{
    private static final int NO_OF_COLUMNS = 2;

    @Inject
    HomePresenter homePresenter;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        TvMazeApplication.get(this).getAppComponent().plus(new HomeModule(this)).inject(this);
        homePresenter.onScreenCreated();
    }

    @Override
    public void showPopularShows(List<Episode> episodes, String country)
    {
        GridLayoutManager layoutManager = new GridLayoutManager(this, NO_OF_COLUMNS);
        layoutManager.setAutoMeasureEnabled(true);
        binding.popularShows.setLayoutManager(layoutManager);
        binding.popularShows.setHasFixedSize(true);
        ShowsAdapter showsAdapter = new ShowsAdapter(episodes);
        binding.popularShows.setAdapter(showsAdapter);
        int spacing = getResources().getDimensionPixelSize(R.dimen.show_grid_spacing);
        binding.popularShows.addItemDecoration(new GridItemDecoration(spacing, NO_OF_COLUMNS));
        binding.popularShows.setNestedScrollingEnabled(false);
        binding.popularShowHeader.setText(String.format(getString(R.string.popular_shows_airing_today), country));
        binding.homeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSchedule(List<Episode> episodes, String scheduleDate)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setAutoMeasureEnabled(true);
        binding.scheduleList.setLayoutManager(layoutManager);
        binding.scheduleList.setHasFixedSize(true);
        EpisodesScheduleAdapter episodesScheduleAdapter = new EpisodesScheduleAdapter(episodes);
        binding.scheduleList.setAdapter(episodesScheduleAdapter);
        binding.scheduleList.setNestedScrollingEnabled(false);
        binding.schedule.setText(String.format(getString(R.string.schedule_for), scheduleDate));
    }

    @Override
    public void showProgress()
    {
        binding.progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress()
    {
        binding.progress.setVisibility(View.GONE);
    }
}
