package com.github.sikv.photos.favorites

import com.github.sikv.photos.domain.ListLayout
import com.github.sikv.photos.domain.Photo

internal data class FavoritesUiState(
    val photos: List<Photo>,
    val listLayout: ListLayout,
    val shouldShowRemovedNotification: Boolean
)
