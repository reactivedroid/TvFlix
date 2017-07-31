package com.android.ashwiask.tvmaze.home;

import java.util.List;

/**
 * @author Ashwini Kumar.
 */

public interface HomeView
{
    void showPopularShows(List<Episode> episodes, String country);

    void showSchedule(List<Episode> episodes, String scheduleDate);

    void showProgress();

    void hideProgress();
}
