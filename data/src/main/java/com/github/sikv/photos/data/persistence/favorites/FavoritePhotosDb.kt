package com.github.sikv.photos.data.persistence.favorites

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.sikv.photos.data.persistence.Converters

@Database(
    entities = [FavoritePhotoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FavoritePhotosDb : RoomDatabase() {
    abstract val favoritePhotosDao: FavoritePhotosDao
}
