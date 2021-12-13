package com.android.tvflix.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.db.favouriteshow.ShowDao

@Database(entities = [FavoriteShow::class], version = 2, exportSchema = false)
abstract class TvFlixDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    companion object {
        const val DATABASE_NAME = "tvflix.db"
    }
}
