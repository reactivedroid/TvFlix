package com.android.tvmaze.db

import android.content.Context
import androidx.room.Room
import com.android.tvmaze.db.favouriteshow.ShowDao
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
object TvMazeDbModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideTvMazeDatabase(context: Context): TvMazeDatabase {
        return Room.databaseBuilder(
            context,
            TvMazeDatabase::class.java, TvMazeDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideShowDao(tvMazeDatabase: TvMazeDatabase): ShowDao {
        return tvMazeDatabase.showDao()
    }
}
