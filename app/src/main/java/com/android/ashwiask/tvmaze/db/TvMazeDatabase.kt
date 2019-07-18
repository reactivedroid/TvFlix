package com.android.ashwiask.tvmaze.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.ashwiask.tvmaze.db.favouriteshow.FavoriteShow
import com.android.ashwiask.tvmaze.db.favouriteshow.ShowDao

@Database(entities = [FavoriteShow::class], version = 2, exportSchema = false)
abstract class TvMazeDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    companion object {
        const val DATABASE_NAME = "tvmaze.db"
    }
}
