package com.android.ashwiask.tvmaze.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;
import com.android.ashwiask.tvmaze.home.Show;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteShowsViewModel extends ViewModel {
    private final FavoriteShowsRepository favoriteShowsRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<FavoriteShow>> favoriteShowsLiveData;
    private List<FavoriteShow> removedFromFavoriteShows;

    @Inject
    public FavoriteShowsViewModel(FavoriteShowsRepository favoriteShowsRepository) {
        this.favoriteShowsRepository = favoriteShowsRepository;
        compositeDisposable = new CompositeDisposable();
        favoriteShowsLiveData = new MutableLiveData<>();
    }

    public void loadFavoriteShows() {
        Disposable favoriteShowsDisposable = favoriteShowsRepository.getAllFavoriteShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFavoritesFetched, this::onError);
        compositeDisposable.add(favoriteShowsDisposable);
    }

    private void onError(Throwable throwable) {
        Log.d("FavoriteShowsViewModel", throwable.getMessage());
    }

    private void onFavoritesFetched(List<FavoriteShow> favoriteShows) {
        removedFromFavoriteShows = new ArrayList<>(favoriteShows.size());
        favoriteShowsLiveData.setValue(favoriteShows);
    }

    public LiveData<List<FavoriteShow>> getFavoriteShowsLiveData() {
        return favoriteShowsLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        removedFromFavoriteShows.clear();
    }

    public void addToFavorite(FavoriteShow show) {
        favoriteShowsRepository.insertIntoFavorites(show);
        removedFromFavoriteShows.remove(show);
    }

    public void removeFromFavorite(FavoriteShow show) {
        favoriteShowsRepository.removeFromFavorites(show);
        removedFromFavoriteShows.add(show);
    }

    public List<Show> getRemovedFromFavoriteShows() {
        List<Show> shows = new ArrayList<>(removedFromFavoriteShows.size());
        for (FavoriteShow favoriteShow : removedFromFavoriteShows) {
            Show show = Show.fromFavoriteShow(favoriteShow)
                    .toBuilder().isFavorite(false).build();
            shows.add(show);
        }
        return shows;
    }
}
