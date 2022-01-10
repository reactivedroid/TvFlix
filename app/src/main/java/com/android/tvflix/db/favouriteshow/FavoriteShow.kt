package com.android.tvflix.db.favouriteshow

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_shows")
data class FavoriteShow(
    @PrimaryKey
    val id: Long,
    val name: String,
    val premiered: String?,
    val imageUrl: String?,
    var summary: String?,
    var rating: String?,
    var runtime: Int?,
    val isFavorite: Boolean
)

