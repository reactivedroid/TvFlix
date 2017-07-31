package com.android.ashwiask.tvmaze.home;

import com.android.ashwiask.tvmaze.common.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Ashwini Kumar.
 */

public class HomePresenterImpl implements HomePresenter
{
    private static final int EIGHT_O_CLOCK = 20;
    private static final String COUNTRY_US = "US";
    private static final String QUERY_DATE_FORMAT = "yyyy-MM-dd";
    private static final String SCHEDULE_DATE_FORMAT = "EEE, MMM d, ''yy";
    private HomeView homeView;
    private CompositeDisposable compositeDisposable;
    private HomeEndpoint homeEndpoint;

    HomePresenterImpl(HomeView homeView, HomeEndpoint homeEndpoint, CompositeDisposable compositeDisposable)
    {
        this.homeView = homeView;
        this.homeEndpoint = homeEndpoint;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onScreenCreated()
    {
        homeView.showProgress();
        String currentDate = getCurrentDate();
        Disposable homeDisposable = homeEndpoint.getCurrentSchedule(COUNTRY_US, currentDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);
        compositeDisposable.add(homeDisposable);
    }

    private static String getCurrentDate()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(QUERY_DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }

    private static String getScheduleDate()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SCHEDULE_DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }

    private void onError(Throwable throwable)
    {
        homeView.hideProgress();
    }

    private void onSuccess(List<Episode> episodes)
    {
        if (isViewAttached())
        {
            List<Episode> filteredList = new ArrayList<>(20);
            for (Episode episode : episodes)
            {
                String[] airtime = episode.getAirtime().split(Constants.SEMICOLON);
                if (Integer.valueOf(airtime[0]) >= EIGHT_O_CLOCK)
                {
                    filteredList.add(episode);
                } else
                {
                    // do nothing
                }
            }
            homeView.hideProgress();
            homeView.showSchedule(filteredList, getScheduleDate());
            homeView.showPopularShows(filteredList, COUNTRY_US);
        } else
        {
            // do nothing
        }

    }


    @Override
    public void onScreenDestroyed()
    {
        homeView = null;
        compositeDisposable.clear();
    }

    private boolean isViewAttached()
    {
        return homeView != null;
    }
}
