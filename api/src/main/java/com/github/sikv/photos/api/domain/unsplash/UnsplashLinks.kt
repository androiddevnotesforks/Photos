package com.github.sikv.photos.api.domain.unsplash

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashLinks(
    @SerializedName("self")
    val self: String,

    @SerializedName("html")
    val html: String,

    @SerializedName("download")
    val download: String
) : Parcelable
