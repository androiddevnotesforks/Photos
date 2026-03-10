package com.github.sikv.photos.data.persistence.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.sikv.photos.data.persistence.Converters

@Database(
    entities = [CachedPhotoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CachedPhotosDb : RoomDatabase() {

    abstract val cachedPhotosDao: CachedPhotosDao
}
