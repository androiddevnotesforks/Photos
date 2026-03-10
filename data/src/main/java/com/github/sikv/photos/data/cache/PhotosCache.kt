package com.github.sikv.photos.data.cache

import com.github.sikv.photos.data.persistence.cache.CachedPhotosDao
import com.github.sikv.photos.domain.Photo
import toCachedPhotoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosCache @Inject constructor(
    private val cachedPhotosDao: CachedPhotosDao
) {

    private companion object {
        const val MAX_CACHE_ITEMS = 1000
        const val CLEANUP_BUFFER = 50 // to avoid cleaning too often.
    }

    suspend fun getById(id: String): Photo? {
        return cachedPhotosDao.getById(id)
    }

    suspend fun insert(photo: Photo) {
        cachedPhotosDao.insert(photo.toCachedPhotoEntity())
        performCleanup()
    }

    suspend fun insertAll(photos: List<Photo>) {
        cachedPhotosDao.insertAll(
            photos.map { photo -> photo.toCachedPhotoEntity() }
        )
        performCleanup()
    }

    private suspend fun performCleanup() {
        val total = cachedPhotosDao.count()

        if (total <= MAX_CACHE_ITEMS + CLEANUP_BUFFER) {
            return
        }

        val deleteCount = total - MAX_CACHE_ITEMS
        cachedPhotosDao.deleteOldest(deleteCount)
    }
}
