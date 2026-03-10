package com.github.sikv.photos.data.persistence.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.sikv.photos.data.persistence.DbConfig

@Dao
interface CachedPhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: CachedPhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<CachedPhotoEntity>)

    @Query("SELECT * FROM ${DbConfig.CACHED_PHOTOS_TABLE} WHERE id=:id")
    suspend fun getById(id: String): CachedPhotoEntity?

    @Query("SELECT COUNT(*) FROM ${DbConfig.CACHED_PHOTOS_TABLE}")
    suspend fun count(): Int

    @Query(
        """
        DELETE FROM ${DbConfig.CACHED_PHOTOS_TABLE}
        WHERE id IN (
            SELECT id FROM ${DbConfig.CACHED_PHOTOS_TABLE}
            ORDER BY cachedAt ASC
            LIMIT :limit
        )
        """
    )
    suspend fun deleteOldest(limit: Int)
}
