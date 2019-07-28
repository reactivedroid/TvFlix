package com.android.ashwiask.tvmaze.db.favouriteshow

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowDao {
    @Query("SELECT * FROM favourite_shows")
    suspend fun allFavouriteShows(): List<FavoriteShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteShow: FavoriteShow)

    @Delete
    suspend fun remove(favoriteShow: FavoriteShow)

    @Query("SELECT id from favourite_shows")
    suspend fun getFavoriteShowIds(): List<Long>

    @Query("DELETE FROM favourite_shows")
    suspend fun clear()
}
