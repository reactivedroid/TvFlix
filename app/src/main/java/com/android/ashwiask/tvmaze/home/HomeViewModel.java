package com.android.ashwiask.tvmaze.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.ashwiask.tvmaze.common.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Ashwini Kumar.
 */

public class HomeViewModel extends ViewModel {
    private static final int EIGHT_O_CLOCK = 20;
    private static final String COUNTRY_US = "US";
    private static final String QUERY_DATE_FORMAT = "yyyy-MM-dd";
    private static final String SCHEDULE_DATE_FORMAT = "EEE, MMM d, ''yy";
    private CompositeDisposable compositeDisposable;
    private TvMazeApi tvMazeApi;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<Episode>> scheduleList;
    private MutableLiveData<List<Episode>> popularShowsList;
    private MutableLiveData<String> errorMsg;

    @Inject
    HomeViewModel(TvMazeApi tvMazeApi) {
        this.tvMazeApi = tvMazeApi;
        scheduleList = new MutableLiveData<>();
        popularShowsList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMsg = new MutableLiveData<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onScreenCreated() {
        isLoading.setValue(true);
        String currentDate = getCurrentDate();
        Disposable homeDisposable = tvMazeApi.getCurrentSchedule(COUNTRY_US, currentDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);
        compositeDisposable.add(homeDisposable);
    }

    private static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(QUERY_DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }

    public String getScheduleDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SCHEDULE_DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }

    private void onError(Throwable throwable) {
        isLoading.setValue(false);
        errorMsg.setValue(throwable.getMessage());
    }

    private void onSuccess(List<Episode> episodes) {
        List<Episode> filteredList = new ArrayList<>(20);
        for (Episode episode : episodes) {
            String[] airtime = episode.airtime().split(Constants.SEMICOLON);
            if (Integer.parseInt(airtime[0]) >= EIGHT_O_CLOCK) {
                filteredList.add(episode);
            }
        }
        isLoading.setValue(false);
        scheduleList.setValue(filteredList);
        popularShowsList.setValue(filteredList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public LiveData<List<Episode>> getPopularShowsList() {
        return popularShowsList;
    }

    public LiveData<List<Episode>> getScheduleList() {
        return scheduleList;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public String getCountry() {
        return COUNTRY_US;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }
}
