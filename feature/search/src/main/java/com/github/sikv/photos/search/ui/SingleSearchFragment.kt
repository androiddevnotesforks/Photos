package com.github.sikv.photos.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.github.sikv.photo.list.ui.PhotoActionDispatcher
import com.github.sikv.photo.list.ui.adapter.PhotoPagingAdapter
import com.github.sikv.photo.list.ui.updateLoadState
import com.github.sikv.photos.common.DownloadService
import com.github.sikv.photos.common.PhotoLoader
import com.github.sikv.photos.common.ui.BaseFragment
import com.github.sikv.photos.common.ui.disableChangeAnimations
import com.github.sikv.photos.common.ui.setVisibilityAnimated
import com.github.sikv.photos.data.repository.FavoritesRepository
import com.github.sikv.photos.domain.Photo
import com.github.sikv.photos.navigation.args.SingleSearchFragmentArguments
import com.github.sikv.photos.navigation.args.fragmentArguments
import com.github.sikv.photos.navigation.route.PhotoDetailsRoute
import com.github.sikv.photos.navigation.route.SetWallpaperRoute
import com.github.sikv.photos.search.SearchQuery
import com.github.sikv.photos.search.SearchViewModel
import com.github.sikv.photos.search.databinding.FragmentSingleSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class SingleSearchFragment : BaseFragment() {

    @Inject
    lateinit var favoritesRepository: FavoritesRepository

    @Inject
    lateinit var downloadService: DownloadService

    @Inject
    lateinit var photoLoader: PhotoLoader

    @Inject
    lateinit var photoDetailsRoute: PhotoDetailsRoute

    @Inject
    lateinit var setWallpaperRoute: SetWallpaperRoute

    private val viewModel: SearchViewModel by activityViewModels()
    private val args by fragmentArguments<SingleSearchFragmentArguments>()

    private val photoActionDispatcher by lazy {
        PhotoActionDispatcher(
            fragment = this,
            downloadService = downloadService,
            photoLoader = photoLoader,
            photoDetailsRoute = photoDetailsRoute,
            setWallpaperRoute = setWallpaperRoute,
            onToggleFavorite = viewModel::toggleFavorite,
            onShowMessage = ::showMessage
        )
    }

    private lateinit var photoAdapter: PhotoPagingAdapter<Photo>

    private var _binding: FragmentSingleSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoAdapter = PhotoPagingAdapter(
            photoLoader = photoLoader,
            favoritesRepository = favoritesRepository,
            lifecycleScope = lifecycleScope,
            listener = photoActionDispatcher
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSingleSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.photosRecycler.adapter = photoAdapter
        binding.photosRecycler.disableChangeAnimations()

        binding.loadingView.isVisible = false
        binding.noResultsView.isVisible = false
        binding.loadingErrorView.isVisible = false

        binding.loadingErrorView.setTryAgainClickListener {
            photoAdapter.retry()
        }

        addLoadStateListener()

        collectSearchQuery()
        collectFavoriteUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun collectSearchQuery() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchQueryState
                    .filterNotNull()
                    .collect { searchQuery ->
                        searchPhotos(searchQuery)
                    }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun collectFavoriteUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteUpdates().collect { update ->
                    when (update) {
                        is FavoritesRepository.UpdatePhoto -> {
                            photoAdapter.notifyPhotoChanged(update.photo)
                        }
                        is FavoritesRepository.UpdateAll -> {
                            photoAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun searchPhotos(searchQuery: SearchQuery) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchPhotos(args.photoSource, searchQuery)?.collect { data ->
                    photoAdapter.submitData(lifecycle, data)
                }
            }
        }
    }

    private fun addLoadStateListener() {
        photoAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.NotLoading -> {
                    binding.photosRecycler.setVisibilityAnimated(View.VISIBLE)
                    binding.noResultsView.isVisible = photoAdapter.itemCount == 0
                }
                is LoadState.Loading -> {
                    binding.noResultsView.isVisible = false
                    binding.photosRecycler.setVisibilityAnimated(View.GONE)
                }
                is LoadState.Error -> {
                    binding.noResultsView.isVisible = false
                    binding.photosRecycler.setVisibilityAnimated(View.GONE)
                }
            }

            binding.loadingView.updateLoadState(loadState)
            binding.loadingErrorView.updateLoadState(loadState)
        }
    }
}
