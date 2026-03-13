package com.github.sikv.photos.curated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.sikv.photos.common.PreferencesService
import com.github.sikv.photos.config.ConfigProvider
import com.github.sikv.photos.data.repository.FavoritesRepository
import com.github.sikv.photos.data.repository.PhotosRepository
import com.github.sikv.photos.domain.ListLayout
import com.github.sikv.photos.domain.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CuratedPhotosViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val favoritesRepository: FavoritesRepository,
    private val preferencesService: PreferencesService,
    private val configProvider: ConfigProvider
) : ViewModel() {

    val curatedPhotosFlow = Pager(
        config = configProvider.getPagingConfig(),
        pagingSourceFactory = {
            CuratedPhotosPagingSource(photosRepository)
        }
    ).flow

    val favoritesFlow = favoritesRepository.getFavoritesSet()

    private val mutableListLayoutState = MutableStateFlow(preferencesService.getCuratedListLayout())
    val listLayoutState: StateFlow<ListLayout> = mutableListLayoutState

    fun toggleFavorite(photo: Photo) {
        viewModelScope.launch {
            favoritesRepository.invertFavorite(photo)
        }
    }

    fun switchListLayout() {
        when (listLayoutState.value) {
            ListLayout.LIST -> {
                preferencesService.setCuratedListLayout(ListLayout.GRID)
                mutableListLayoutState.value = ListLayout.GRID
            }
            ListLayout.GRID -> {
                preferencesService.setCuratedListLayout(ListLayout.LIST)
                mutableListLayoutState.value = ListLayout.LIST
            }
        }
    }
}

private fun ConfigProvider.getPagingConfig(): PagingConfig {
    val page = getPageConfig()

    return PagingConfig(
        initialLoadSize = page.initialLoadSize,
        pageSize = page.pageSize,
        enablePlaceholders = page.enablePlaceholders
    )
}
