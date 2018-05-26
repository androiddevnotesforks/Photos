package com.github.sikv.photos.data

import com.github.sikv.photos.BuildConfig
import com.github.sikv.photos.model.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PhotosService {

    @Headers("Authorization: Client-ID ${BuildConfig.ACCESS_KEY}")
    @GET("photos")
    fun getPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int, @Query("order_by") orderBy: String): Call<List<Photo>>
}