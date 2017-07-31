package com.android.ashwiask.tvmaze.home;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.ashwiask.tvmaze.R;
import com.android.ashwiask.tvmaze.TvMazeApplication;
import com.android.ashwiask.tvmaze.common.GridItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeView
{
    private static final int NO_OF_COLUMS = 2;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.popular_shows)
    RecyclerView popularShows;
    @BindView(R.id.schedule_list)
    RecyclerView episodeSchedules;
    @BindView(R.id.schedule)
    TextView schedule;
    @BindView(R.id.popular_show_header)
    TextView popularShowHeader;
    @BindView(R.id.home_container)
    NestedScrollView homeContainer;

    @Inject
    HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        TvMazeApplication.get(this).getAppComponent().plus(new HomeModule(this)).inject(this);
        homePresenter.onScreenCreated();
    }

    @Override
    public void showPopularShows(List<Episode> episodes, String country)
    {
        GridLayoutManager layoutManager = new GridLayoutManager(this, NO_OF_COLUMS);
        layoutManager.setAutoMeasureEnabled(true);
        popularShows.setLayoutManager(layoutManager);
        popularShows.setHasFixedSize(true);
        ShowsAdapter showsAdapter = new ShowsAdapter(episodes);
        popularShows.setAdapter(showsAdapter);
        int spacing = getResources().getDimensionPixelSize(R.dimen.show_grid_spacing);
        popularShows.addItemDecoration(new GridItemDecoration(spacing, NO_OF_COLUMS));
        popularShows.setNestedScrollingEnabled(false);
        popularShowHeader.setText(String.format(getString(R.string.popular_shows_airing_today), country));
        homeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSchedule(List<Episode> episodes, String scheduleDate)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setAutoMeasureEnabled(true);
        episodeSchedules.setLayoutManager(layoutManager);
        episodeSchedules.setHasFixedSize(true);
        EpisodesScheduleAdapter episodesScheduleAdapter = new EpisodesScheduleAdapter(episodes);
        episodeSchedules.setAdapter(episodesScheduleAdapter);
        episodeSchedules.setNestedScrollingEnabled(false);
        schedule.setText(String.format(getString(R.string.schedule_for), scheduleDate));
    }

    @Override
    public void showProgress()
    {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress()
    {
        progress.setVisibility(View.GONE);
    }
}
