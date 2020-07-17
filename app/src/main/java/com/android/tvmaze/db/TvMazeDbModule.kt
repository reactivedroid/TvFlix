package com.android.tvmaze.db

import android.content.Context
import androidx.room.Room
import com.android.tvmaze.db.favouriteshow.ShowDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object TvMazeDbModule {
    @Singleton
    @Provides
    fun provideTvMazeDatabase(@ApplicationContext context: Context): TvMazeDatabase {
        return Room.databaseBuilder(
            context,
            TvMazeDatabase::class.java, TvMazeDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideShowDao(tvMazeDatabase: TvMazeDatabase): ShowDao {
        return tvMazeDatabase.showDao()
    }
}
