package com.android.ashwiask.tvmaze.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.ashwiask.tvmaze.common.Constants;
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;
import com.android.ashwiask.tvmaze.favorite.FavoriteShowsRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
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
    private final FavoriteShowsRepository favoriteShowsRepository;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<Episode>> scheduleList;
    private MutableLiveData<List<Show>> popularShowsList;
    private MutableLiveData<String> errorMsg;

    @Inject
    HomeViewModel(TvMazeApi tvMazeApi,
                  FavoriteShowsRepository favoriteShowsRepository) {
        this.tvMazeApi = tvMazeApi;
        this.favoriteShowsRepository = favoriteShowsRepository;
        scheduleList = new MutableLiveData<>();
        popularShowsList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMsg = new MutableLiveData<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onScreenCreated() {
        isLoading.setValue(true);
        String currentDate = getCurrentDate();
        Single<List<Show>> favoriteShows = favoriteShowsRepository.getAllFavoriteShows()
                .map(this::convertToShows);
        Single<List<Episode>> episodes = tvMazeApi.getCurrentSchedule(COUNTRY_US, currentDate);
        Single<List<Episode>> zippedSingleSource = Single.zip(episodes, favoriteShows, this::favorites);
        Disposable homeDisposable = zippedSingleSource
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccess, this::onError);
        compositeDisposable.add(homeDisposable);
    }

    private List<Episode> favorites(List<Episode> episodes, List<Show> favoriteShows) {
        List<Episode> updatedEpisodes = new ArrayList<>(episodes.size());
        for (Episode episode : episodes) {
            Show show = episode.show();
            if (favoriteShows.contains(episode.show())) {
                show = show.toBuilder()
                        .isFavorite(true)
                        .build();
                episode = episode.toBuilder()
                        .show(show)
                        .build();
            }
            updatedEpisodes.add(episode);
        }

        return updatedEpisodes;
    }

    private List<Show> convertToShows(List<FavoriteShow> favouriteShows) {
        List<Show> favoriteShows = new ArrayList<>(favouriteShows.size());
        for (FavoriteShow favoriteShow : favouriteShows) {
            Show show = Show.fromFavoriteShow(favoriteShow);
            favoriteShows.add(show);
        }
        return favoriteShows;
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
        List<Episode> filteredList = new ArrayList<>(episodes.size());
        List<Show> filteredShowsList = new ArrayList<>(episodes.size());
        for (Episode episode : episodes) {
            String[] airtime = episode.airtime().split(Constants.SEMICOLON);
            if (Integer.parseInt(airtime[0]) >= EIGHT_O_CLOCK &&
                    episode.show().image() != null) {
                filteredList.add(episode);
                filteredShowsList.add(episode.show());
            }
        }
        isLoading.setValue(false);
        scheduleList.setValue(filteredList);
        popularShowsList.setValue(filteredShowsList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public LiveData<List<Show>> getPopularShowsList() {
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

    public void addToFavorite(Show show) {
        favoriteShowsRepository.insertShowIntoFavorites(show);
    }

    public void removeFromFavorite(Show show) {
        favoriteShowsRepository.removeShowFromFavorites(show);
    }
}
