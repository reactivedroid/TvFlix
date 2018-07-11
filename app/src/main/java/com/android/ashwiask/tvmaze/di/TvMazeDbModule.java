package com.android.ashwiask.tvmaze.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.android.ashwiask.tvmaze.db.TvMazeDatabase;
import com.android.ashwiask.tvmaze.db.favouriteshow.ShowDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TvMazeDbModule {
    @Singleton
    @Provides
    public TvMazeDatabase provideTvMazeDatabase(Context context) {
        return Room.databaseBuilder(context,
                TvMazeDatabase.class, TvMazeDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    public ShowDao provideShowDao(TvMazeDatabase tvMazeDatabase) {
        return tvMazeDatabase.showDao();
    }
}
