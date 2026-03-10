package com.github.sikv.photos.data.persistence.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.sikv.photos.data.persistence.DbConfig
import com.github.sikv.photos.data.persistence.favorites.FavoritePhotoEntity
import com.github.sikv.photos.domain.Photo
import com.github.sikv.photos.domain.PhotoSource
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = DbConfig.CACHED_PHOTOS_TABLE,
    indices = [Index(value = ["cachedAt"])]
)
@Parcelize
class CachedPhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "previewUrl")
    val previewUrl: String,

    @ColumnInfo(name = "fullPreviewUrl")
    val fullPreviewUrl: String,

    @ColumnInfo(name = "downloadUrl")
    val downloadUrl: String,

    @ColumnInfo(name = "shareUrl")
    val shareUrl: String,

    @ColumnInfo(name = "photographerName")
    val photographerName: String,

    @ColumnInfo(name = "photographerImageUrl")
    val photographerImageUrl: String?,

    @ColumnInfo(name = "photographerUrl")
    val photographerUrl: String?,

    @ColumnInfo(name = "source")
    val source: PhotoSource,

    @ColumnInfo(name = "cachedAt")
    val cachedAt: Long = System.currentTimeMillis()
) : Photo() {

    override fun getPhotoId(): String = id

    override fun getPhotoPreviewUrl(): String = previewUrl
    override fun getPhotoFullPreviewUrl(): String = fullPreviewUrl
    override fun getPhotoDownloadUrl(): String = downloadUrl
    override fun getPhotoShareUrl(): String = shareUrl

    override fun getPhotoPhotographerName(): String = photographerName
    override fun getPhotoPhotographerImageUrl(): String? = photographerImageUrl
    override fun getPhotoPhotographerUrl(): String? = photographerUrl

    override fun getPhotoSource(): PhotoSource = source
}
