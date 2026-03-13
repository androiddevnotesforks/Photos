package com.github.sikv.photos.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.sikv.photos.domain.ListLayout
import com.github.sikv.photos.domain.Photo

@Composable
fun DynamicPhotoItem(
    photo: Photo,
    favorites: Set<String>,
    listLayout: ListLayout,
    onPhotoClick: (Photo) -> Unit,
    onPhotoAttributionClick: (Photo) -> Unit,
    onPhotoActionsClick: (Photo) -> Unit,
    onToggleFavoriteClick: (Photo) -> Unit,
    onSharePhotoClick: (Photo) -> Unit,
    onDownloadPhotoClick: (Photo) -> Unit
) {

    val favorite = remember(favorites, photo.getPhotoId()) {
        favorites.contains(photo.getPhotoId())
    }

    when (listLayout) {
        ListLayout.LIST -> {
            PhotoItem(
                photo = photo,
                isFavorite = favorite,
                onClick = {
                    onPhotoClick(photo)
                },
                onAttributionClick = {
                    onPhotoAttributionClick(photo)
                },
                onMoreClick = {
                    onPhotoActionsClick(photo)
                },
                onToggleFavorite = {
                    onToggleFavoriteClick(photo)
                },
                onShareClick = {
                    onSharePhotoClick(photo)
                },
                onDownloadClick = {
                    onDownloadPhotoClick(photo)
                }
            )
        }
        ListLayout.GRID -> {
            PhotoItemCompact(
                photo = photo,
                onClick = {
                    onPhotoClick(photo)
                }
            )
        }
    }
}
