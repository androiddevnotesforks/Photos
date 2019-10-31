package com.github.sikv.photos.api

import com.github.sikv.photos.model.UnsplashPhoto
import com.github.sikv.photos.model.UnsplashSearchResponse
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashClient @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun getLatestPhotos(page: Int, perPage: Int): Call<List<UnsplashPhoto>> =
            unsplashApi.getPhotos(page, perPage, "latest")

    fun getPhoto(id: String): Call<UnsplashPhoto> =
            unsplashApi.getPhoto(id)

    fun searchPhotos(query: String, page: Int, perPage: Int): Call<UnsplashSearchResponse> =
            unsplashApi.searchPhotos(query, page, perPage)
}