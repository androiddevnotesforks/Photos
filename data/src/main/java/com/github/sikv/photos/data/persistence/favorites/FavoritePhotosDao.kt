package com.github.sikv.photos.data.persistence.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.github.sikv.photos.data.persistence.DbConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: FavoritePhotoEntity)

    @RawQuery(observedEntities = [FavoritePhotoEntity::class])
    fun getPhotos(query: SupportSQLiteQuery): Flow<List<FavoritePhotoEntity>>

    @Query("SELECT * FROM ${DbConfig.FAVORITE_PHOTOS_TABLE} WHERE id=:id AND markedAsDeleted=0")
    suspend fun getById(id: String): FavoritePhotoEntity?

    @Query("SELECT * FROM ${DbConfig.FAVORITE_PHOTOS_TABLE} WHERE markedAsDeleted=0 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandom(): FavoritePhotoEntity?

    @Query("SELECT COUNT(*) FROM ${DbConfig.FAVORITE_PHOTOS_TABLE} WHERE markedAsDeleted=0")
    suspend fun getCount(): Int

    @Query("UPDATE ${DbConfig.FAVORITE_PHOTOS_TABLE} SET markedAsDeleted=1")
    suspend fun markAllAsDeleted()

    @Query("UPDATE ${DbConfig.FAVORITE_PHOTOS_TABLE} SET markedAsDeleted=0")
    suspend fun unmarkAllAsDeleted()

    @Delete
    suspend fun delete(photo: FavoritePhotoEntity)

    @Query("DELETE from ${DbConfig.FAVORITE_PHOTOS_TABLE} WHERE markedAsDeleted=1")
    suspend fun deleteAllMarked()
}
