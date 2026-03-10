package com.github.sikv.photos.data.persistence

import android.content.Context
import androidx.room.Room
import com.github.sikv.photos.data.persistence.cache.CachedPhotosDao
import com.github.sikv.photos.data.persistence.cache.CachedPhotosDb
import com.github.sikv.photos.data.persistence.favorites.FavoritePhotosDao
import com.github.sikv.photos.data.persistence.favorites.FavoritePhotosDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    companion object {
        private const val FAVORITE_PHOTOS_DB = "favorites.db"
        private const val CACHED_PHOTOS_DB = "cached.db"
    }

    @Provides
    @Singleton
    fun provideFavoritePhotosDatabase(@ApplicationContext context: Context): FavoritePhotosDb {
        return Room
            .databaseBuilder(
                context.applicationContext,
                FavoritePhotosDb::class.java,
                FAVORITE_PHOTOS_DB
            )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideCachedPhotosDatabase(@ApplicationContext context: Context): CachedPhotosDb {
        return Room
            .databaseBuilder(
                context.applicationContext,
                CachedPhotosDb::class.java,
                CACHED_PHOTOS_DB
            )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideFavoritePhotosDao(favoritesDb: FavoritePhotosDb): FavoritePhotosDao {
        return favoritesDb.favoritePhotosDao
    }

    @Provides
    fun provideCachedPhotosDao(cachedPhotosDb: CachedPhotosDb): CachedPhotosDao {
        return cachedPhotosDb.cachedPhotosDao
    }
}
