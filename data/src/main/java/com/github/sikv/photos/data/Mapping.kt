import com.github.sikv.photos.data.PhotoDTO
import com.github.sikv.photos.data.PhotosDTO
import com.github.sikv.photos.data.persistence.cache.CachedPhotoEntity
import com.github.sikv.photos.data.persistence.favorites.FavoritePhotoEntity
import com.github.sikv.photos.domain.Photo
import com.google.protobuf.StringValue

internal fun List<Photo>.toDTO(): PhotosDTO {
    return PhotosDTO
        .newBuilder()
        .addAllPhotos(map { photo -> photo.toDTO() })
        .build()
}

internal fun Photo.toDTO(): PhotoDTO {
    return PhotoDTO
        .newBuilder()
        .apply {
            setId(getPhotoId())
        }
        .build()
}

internal fun StringValue.toDomain(): String? {
    return value.takeIf { it.isNotEmpty() }
}

internal fun Photo.toFavoritePhotoEntity(): FavoritePhotoEntity {
    return FavoritePhotoEntity(
        id = getPhotoId(),
        source = getPhotoSource()
    )
}

internal fun Photo.toCachedPhotoEntity(): CachedPhotoEntity {
    return CachedPhotoEntity(
        id = getPhotoId(),
        previewUrl = getPhotoPreviewUrl(),
        fullPreviewUrl = getPhotoFullPreviewUrl(),
        downloadUrl = getPhotoDownloadUrl(),
        shareUrl = getPhotoShareUrl(),
        photographerName = getPhotoPhotographerName(),
        photographerImageUrl = getPhotoPhotographerImageUrl(),
        photographerUrl = getPhotoPhotographerUrl(),
        source = getPhotoSource()
    )
}
