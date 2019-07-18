package com.android.ashwiask.tvmaze.db.favouriteshow

import androidx.room.*
import io.reactivex.Single

@Dao
interface ShowDao {
    @Query("SELECT * FROM favourite_shows")
    fun allFavouriteShows(): Single<List<FavoriteShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteShow: FavoriteShow)

    @Delete
    fun remove(favoriteShow: FavoriteShow)

    @Query("SELECT id from favourite_shows")
    fun getFavoriteShowIds(): Single<List<Long>>
}
