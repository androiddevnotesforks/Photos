package com.github.sikv.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sikv.photos.config.ConfigProvider
import com.github.sikv.photos.data.repository.PhotosRepository
import com.github.sikv.photos.model.Photo
import com.github.sikv.photos.service.RecommendationsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RecommendationsUiState {

    object Loading : RecommendationsUiState

    data class Data(
        val photos: List<Photo>,
        val isNextPageLoading: Boolean
    ) : RecommendationsUiState
}

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val recommendationService: RecommendationsService,
    private val configProvider: ConfigProvider
) : ViewModel() {

    private val mutableUiState = MutableStateFlow<RecommendationsUiState>(
        RecommendationsUiState.Data(
            photos = emptyList(),
            isNextPageLoading = false
        )
    )
    val uiState: StateFlow<RecommendationsUiState> = mutableUiState

    init {
        mutableUiState.value = RecommendationsUiState.Loading

        loadRecommendations()
    }

    fun loadRecommendations(refresh: Boolean = false) {
        viewModelScope.launch {
            val recommendation = recommendationService.getNextRecommendation()

            if (recommendation.query != null) {
                setLoadingState(refresh)

                val photos = photosRepository.searchPhotos(
                    recommendation.query,
                    configProvider.getRecommendationsLimit()
                )

                appendPhotos(photos)
            } else {
                appendPhotos(emptyList())
            }
        }
    }

    private fun setLoadingState(refresh: Boolean) {
        if (refresh) {
            mutableUiState.value = RecommendationsUiState.Loading
        } else {
            mutableUiState.value = when (val currentState = mutableUiState.value) {
                RecommendationsUiState.Loading -> {
                    RecommendationsUiState.Loading
                }
                is RecommendationsUiState.Data -> {
                    RecommendationsUiState.Data(
                        photos = currentState.photos,
                        isNextPageLoading = true
                    )
                }
            }
        }
    }

    private fun appendPhotos(photos: List<Photo>) {
        mutableUiState.value = when (val currentState = mutableUiState.value) {
            RecommendationsUiState.Loading -> {
                RecommendationsUiState.Data(
                    photos = photos,
                    isNextPageLoading = false
                )
            }
            is RecommendationsUiState.Data -> {
                RecommendationsUiState.Data(
                    photos = currentState.photos + photos,
                    isNextPageLoading = false
                )
            }
        }
    }
}