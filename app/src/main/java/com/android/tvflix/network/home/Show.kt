package com.android.tvflix.network.home

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Show(
    val id: Long,
    val url: String?,
    val name: String,
    val type: String?,
    val language: String?,
    val genres: List<String>?,
    val status: String?,
    val runtime: Int?,
    val premiered: String?,
    val officialSite: String?,
    @Json(name = "network") val airChannel: Channel?,
    val webChannel: Channel?,
    val image: Map<String, String>?,
    @Json(name = "externals") val externalInfo: ExternalInfo?,
    val summary: String?,
    val rating: Map<String, String>?
) : Parcelable {
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Channel(
        val id: Long?,
        val name: String?,
        val country: Country?
    ) : Parcelable {
        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Country(
            val name: String,
            val code: String,
            val timezone: String
        ) : Parcelable
    }

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class ExternalInfo(
        val tvrage: String?,
        val thetvdb: Long?,
        val imdb: String?
    ) : Parcelable
}