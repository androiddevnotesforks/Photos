package com.github.sikv.photos.data.repository.impl

import com.github.sikv.photos.data.SortBy
import com.github.sikv.photos.data.cache.PhotosCache
import com.github.sikv.photos.data.persistence.favorites.FavoritePhotosDao
import com.github.sikv.photos.data.persistence.favorites.FavoritePhotosDbQueryBuilder
import com.github.sikv.photos.data.repository.FavoritesRepository2
import com.github.sikv.photos.data.repository.PhotosRepository
import com.github.sikv.photos.domain.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import toFavoritePhotoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository2Impl @Inject constructor(
    private val favoritePhotosDao: FavoritePhotosDao,
    private val queryBuilder: FavoritePhotosDbQueryBuilder,
    private val photosRepository: PhotosRepository,
    private val photosCache: PhotosCache
) : FavoritesRepository2 {

    override suspend fun isFavorite(photo: Photo): Boolean {
        return favoritePhotosDao.getById(photo.getPhotoId()) != null
    }

    override suspend fun invertFavorite(photo: Photo) {
        val favorite = isFavorite(photo)
        if (favorite) {
            favoritePhotosDao.delete(photo.toFavoritePhotoEntity())
        } else {
            favoritePhotosDao.insert(photo.toFavoritePhotoEntity())
            photosCache.insert(photo)
        }
    }

    override fun getFavorites(sortBy: SortBy): Flow<List<Photo>> {
        val query = queryBuilder.buildGetPhotosQuery(sortBy)

        return favoritePhotosDao
            .getPhotos(query)
            .map { favoriteList ->
                // TODO: Optimize this.
                favoriteList.mapNotNull { favoritePhoto ->
                    photosRepository.getPhoto(
                        id = favoritePhoto.id,
                        source = favoritePhoto.source
                    ).resultOrNull()
                }
            }
    }

    override suspend fun getRandom(): Photo? {
        val randomFavorite = favoritePhotosDao.getRandom() ?: return null

        return photosRepository
            .getPhoto(
                id = randomFavorite.id,
                source = randomFavorite.source
            )
            .resultOrNull()
    }

    override suspend fun markAllAsDeleted(): Boolean {
        val count = favoritePhotosDao.getCount()

        return if (count > 0) {
            favoritePhotosDao.markAllAsDeleted()
            true
        } else {
            false
        }
    }

    override suspend fun unmarkAllAsDeleted() {
        favoritePhotosDao.unmarkAllAsDeleted()
    }

    override suspend fun deleteAllMarked() {
        favoritePhotosDao.deleteAllMarked()
    }
}
