package com.github.sikv.photos.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.sikv.photos.domain.PhotoSource
import com.github.sikv.photos.domain.Photo
import kotlinx.parcelize.Parcelize

@Entity(tableName = DbConfig.favoritePhotosTableName)
@Parcelize
class FavoritePhotoEntity(
    @PrimaryKey
    var id: String,

    var previewUrl: String,
    var fullPreviewUrl: String,
    var downloadUrl: String,
    var shareUrl: String,

    var photographerName: String,
    var photographerImageUrl: String?,
    var photographerUrl: String?,

    var source: PhotoSource,

    // Sorting options
    val dateAdded: Long = System.currentTimeMillis(),

    // Remove/Undo operations
    val markedAsDeleted: Boolean = false
) : Photo() {

    companion object {
        fun fromPhoto(photo: Photo): FavoritePhotoEntity {
            return FavoritePhotoEntity(
                id = photo.getPhotoId(),
                previewUrl = photo.getPhotoPreviewUrl(),
                fullPreviewUrl = photo.getPhotoFullPreviewUrl(),
                downloadUrl = photo.getPhotoDownloadUrl(),
                shareUrl = photo.getPhotoShareUrl(),
                photographerName = photo.getPhotoPhotographerName(),
                photographerImageUrl = photo.getPhotoPhotographerImageUrl(),
                photographerUrl = photo.getPhotoPhotographerUrl(),
                source = photo.getPhotoSource()
            )
        }
    }

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
