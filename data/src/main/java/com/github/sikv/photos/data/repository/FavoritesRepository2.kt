package com.github.sikv.photos.data.repository

import com.github.sikv.photos.data.SortBy
import com.github.sikv.photos.domain.Photo
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository2 {

    suspend fun isFavorite(photo: Photo): Boolean
    suspend fun invertFavorite(photo: Photo)

    fun getFavorites(sortBy: SortBy = SortBy.DATE_ADDED_NEWEST): Flow<List<Photo>>
    suspend fun getRandom(): Photo?

    suspend fun markAllAsDeleted(): Boolean
    suspend fun unmarkAllAsDeleted()
    suspend fun deleteAllMarked()
}
