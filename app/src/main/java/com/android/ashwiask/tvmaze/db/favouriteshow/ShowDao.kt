package com.android.ashwiask.tvmaze.db.favouriteshow

import androidx.room.*

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
}
