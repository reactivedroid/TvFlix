package com.android.ashwiask.tvmaze.db.favouriteshow;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ShowDao {
    @Query("SELECT * FROM favourite_shows")
    Single<List<FavoriteShow>> getAllFavouriteShows();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteShow favoriteShow);

    @Delete
    void remove(FavoriteShow favoriteShow);

    @Query("SELECT count(*) FROM favourite_shows where id LIKE :id")
    int isFavouriteShow(long id);
}
