package com.android.ashwiask.tvmaze.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow;
import com.android.ashwiask.tvmaze.db.favouriteshow.ShowDao;

@Database(entities = {FavoriteShow.class}, version = 1, exportSchema = false)
public abstract class TvMazeDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "tvmaze.db";

    public abstract ShowDao showDao();
}
