package com.github.sikv.photos.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.github.sikv.photos.compose.ui.Scaffold
import com.github.sikv.photos.compose.ui.Spacing
import com.github.sikv.photos.domain.Photo
import com.github.sikv.photos.recommendations.Recommendations
import com.github.sikv.photos.search.R

@Composable
internal fun SearchDashboardScreen(
    onSearchClick: () -> Unit,
    onVoiceSearchClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    recommendationsEnabled: Boolean
) {
    Scaffold(
        title = {
            SearchBar(
                onSearchClick = onSearchClick
            )
        },
        actions = {
            IconButton(
                onClick = onVoiceSearchClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_keyboard_voice_24dp),
                    contentDescription = stringResource(id = R.string.voice_search)
                )
            }
        },
    ) {
        if (recommendationsEnabled) {
            Recommendations(
                onPhotoClick = onPhotoClick,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun SearchBar(
    onSearchClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSearchClick)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search_24dp),
            contentDescription = stringResource(id = R.string.search)
        )
        Spacer(modifier = Modifier.width(Spacing.Two))
        Text(
            text = stringResource(id = R.string.search_for_photos),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.outline
            )
        )
    }
}
