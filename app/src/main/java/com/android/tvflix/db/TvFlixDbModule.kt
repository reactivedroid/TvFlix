package com.android.tvflix.db

import android.content.Context
import androidx.room.Room
import com.android.tvflix.db.favouriteshow.ShowDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TvFlixDbModule {
    @Singleton
    @Provides
    fun provideTvFlixDb(@ApplicationContext context: Context): TvFlixDatabase {
        return Room.databaseBuilder(
            context,
            TvFlixDatabase::class.java, TvFlixDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideShowDao(tvFlixDatabase: TvFlixDatabase): ShowDao {
        return tvFlixDatabase.showDao()
    }
}
