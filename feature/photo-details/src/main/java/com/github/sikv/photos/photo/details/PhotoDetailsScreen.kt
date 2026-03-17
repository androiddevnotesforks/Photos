package com.github.sikv.photos.photo.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.sikv.photos.common.ui.ActionIconButton
import com.github.sikv.photos.common.ui.NetworkImage
import com.github.sikv.photos.common.ui.PlaceholderImage
import com.github.sikv.photos.common.ui.TransparentTopAppBar
import com.github.sikv.photos.common.ui.getAttributionPlaceholderBackgroundColor
import com.github.sikv.photos.common.ui.getAttributionPlaceholderTextColor
import com.github.sikv.photos.compose.ui.FavoriteButton
import com.github.sikv.photos.domain.Photo

private const val actionableContentAlpha = 0.9f

@Composable
internal fun PhotoDetailsScreen(
    onBackClick: () -> Unit,
    onPhotoAttributionClick: (Photo) -> Unit,
    onSharePhotoClick: (Photo) -> Unit,
    onDownloadPhotoClick: (Photo) -> Unit,
    onSetWallpaperClick: (Photo) -> Unit,
    viewModel: PhotoDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box {
        NetworkImage(
            imageUrl = uiState.photo.getPhotoFullPreviewUrl(),
            loading = {
                // Show smaller version while the full preview is loading.
                NetworkImage(
                    imageUrl = uiState.photo.getPhotoPreviewUrl(),
                    modifier = Modifier
                        .fillMaxSize()
                )
            },
            modifier = Modifier
                .fillMaxSize()
        )
        TransparentTopAppBar(
            onBackPressed = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
        )
        ActionableContent(
            photo = uiState.photo,
            isFavorite = uiState.isFavorite,
            onToggleFavorite = viewModel::toggleFavorite,
            onShareClick = {
                onSharePhotoClick(uiState.photo)
            },
            onDownloadClick = {
                onDownloadPhotoClick(uiState.photo)
            },
            onSetWallpaperClick = {
                onSetWallpaperClick(uiState.photo)
            },
            onAttributionClick = {
                onPhotoAttributionClick(uiState.photo)
            },
            modifier = Modifier
                .navigationBarsPadding()
                .padding(12.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = actionableContentAlpha),
                    shape = Shapes().large,
                )
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ActionableContent(
    photo: Photo,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onSetWallpaperClick: () -> Unit,
    onAttributionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Attribution(
                photo = photo,
                onAttributionClick = onAttributionClick
            )
            SecondaryActions(
                isFavorite = isFavorite,
                onToggleFavorite = onToggleFavorite,
                onSharePressed = onShareClick
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryActions(
            onDownloadPressed = onDownloadClick,
            onSetWallpaperPressed = onSetWallpaperClick
        )
    }
}

@Composable
private fun RowScope.Attribution(
    photo: Photo,
    onAttributionClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onAttributionClick() }
            .padding(end = 8.dp)
            .weight(1f)
    ) {
        val photographerImageUrl = photo.getPhotoPhotographerImageUrl()

        val modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)

        if (photographerImageUrl != null) {
            NetworkImage(
                imageUrl = photographerImageUrl,
                modifier = modifier
            )
        } else {
            PlaceholderImage(
                text = photo.getPhotoPhotographerName().first().uppercaseChar().toString(),
                textColor = getAttributionPlaceholderTextColor(LocalContext.current),
                backgroundColor = getAttributionPlaceholderBackgroundColor(LocalContext.current),
                modifier = modifier
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(photo.getPhotoPhotographerName(),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(photo.getPhotoSource().url,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PrimaryActions(
    onDownloadPressed: () -> Unit,
    onSetWallpaperPressed: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = onDownloadPressed
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_file_download_24dp),
                contentDescription = stringResource(id = R.string.content_description_download),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(stringResource(id = R.string.download))
        }
        Spacer(modifier = Modifier.width(24.dp))
        Button(
            onClick = onSetWallpaperPressed
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_wallpaper),
                contentDescription = stringResource(id = R.string.content_description_set_wallpaper),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(stringResource(id = R.string.wallpaper))
        }
    }
}

@Composable
private fun SecondaryActions(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSharePressed: () -> Unit
) {
    Row {
        ActionIconButton(
            icon = R.drawable.ic_share_24dp,
            contentDescription = R.string.content_description_share,
            onClick = onSharePressed
        )

        Spacer(modifier = Modifier.width(8.dp))

        FavoriteButton(
            isFavorite = isFavorite,
            onToggleFavorite = onToggleFavorite
        )
    }
}
