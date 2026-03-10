package com.github.sikv.photos.data.persistence.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.sikv.photos.data.persistence.DbConfig
import com.github.sikv.photos.domain.PhotoSource

@Entity(tableName = DbConfig.FAVORITE_PHOTOS_TABLE)
class FavoritePhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "source")
    var source: PhotoSource,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "markedAsDeleted")
    val markedAsDeleted: Boolean = false // For Delete/Undo operations.
)
