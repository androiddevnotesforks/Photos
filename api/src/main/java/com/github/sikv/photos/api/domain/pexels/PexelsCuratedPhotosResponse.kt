package com.github.sikv.photos.api.domain.pexels

import com.google.gson.annotations.SerializedName

data class PexelsCuratedPhotosResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("par_page")
    val perPage: Int,

    @SerializedName("photos")
    val photos: List<PexelsPhoto>
)
