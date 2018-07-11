package com.android.ashwiask.tvmaze.favorite;

import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;
import com.android.ashwiask.tvmaze.db.favouriteshow.ShowDao;
import com.android.ashwiask.tvmaze.home.Show;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class FavoriteShowsRepository {
    private final ShowDao showDao;

    @Inject
    public FavoriteShowsRepository(ShowDao showDao) {
        this.showDao = showDao;
    }

    public Single<List<FavoriteShow>> getAllFavoriteShows() {
        return showDao.getAllFavouriteShows();
    }

    public boolean isFavoriteShow(long id) {
        return showDao.isFavouriteShow(id) > 0;
    }

    public void insertShowIntoFavorites(Show show) {
        FavoriteShow favoriteShow = new FavoriteShow();
        favoriteShow.setId(show.id());
        favoriteShow.setImageUrl(show.image().get("original"));
        favoriteShow.setRating(show.rating().get("average"));
        favoriteShow.setName(show.name());
        favoriteShow.setPremiered(show.premiered());
        favoriteShow.setSummary(show.summary());
        favoriteShow.setFavorite(true);
        showDao.insert(favoriteShow);
    }

    public void removeShowFromFavorites(Show show) {
        FavoriteShow favoriteShow = new FavoriteShow();
        favoriteShow.setId(show.id());
        favoriteShow.setImageUrl(show.image().get("original"));
        favoriteShow.setRating(show.rating().get("average"));
        favoriteShow.setName(show.name());
        favoriteShow.setPremiered(show.premiered());
        favoriteShow.setSummary(show.summary());
        favoriteShow.setFavorite(false);
        showDao.remove(favoriteShow);
    }

    public void insertIntoFavorites(FavoriteShow favoriteShow) {
        showDao.insert(favoriteShow);
    }

    public void removeFromFavorites(FavoriteShow favoriteShow) {
        showDao.remove(favoriteShow);
    }
}
