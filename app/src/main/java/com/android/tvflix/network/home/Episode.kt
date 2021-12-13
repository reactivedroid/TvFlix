package com.android.tvflix.network.home

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Episode(
    val show: Show,
    val id: Long,
    val url: String?,
    val name: String?,
    val season: Int?,
    val number: Int?,
    val airdate: String?,
    val airtime: String?,
    val runtime: Int?
) : Parcelable