package com.github.sikv.photos.ui.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.sikv.photos.App
import com.github.sikv.photos.R
import com.github.sikv.photos.ui.fragment.*
import com.github.sikv.photos.util.SetWallpaperState
import com.github.sikv.photos.util.customTag
import kotlinx.android.synthetic.main.activity_main.*

// TODO Use ViewModel
class MainActivity : BaseActivity() {

    companion object {
        private const val ACTION_SEARCH = "com.github.sikv.photos.action.SEARCH"

        private const val PHOTOS_FRAGMENT_INDEX = 0
        private const val QUEUE_FRAGMENT_INDEX = 1
        private const val SEARCH_FRAGMENT_INDEX = 2
        private const val FAVORITES_FRAGMENT_INDEX = 3
        private const val MORE_FRAGMENT_INDEX = 4

        private const val PHOTOS_ITEM_ID = R.id.photos
        private const val SEARCH_ITEM_ID = R.id.search

        private const val KEY_FRAGMENT_TAG = "key_fragment_tag"
    }

    private val fragments = listOf(
            PhotosFragment(),
            QueueFragment(),
            SearchFragment(),
            FavoritesFragment(),
            MoreFragment()
    )

    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var initialFragmentIndex = PHOTOS_FRAGMENT_INDEX
        var initialItemId = PHOTOS_ITEM_ID

        if (intent?.action.equals(ACTION_SEARCH)) {
            initialFragmentIndex = SEARCH_FRAGMENT_INDEX
            initialItemId = SEARCH_ITEM_ID
        }

        if (savedInstanceState == null) {
            setupBottomNavigation(initialFragmentIndex, initialItemId)
        }

        setNavigationItemSelectedListener()

        observeSetWallpaperInProgress()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        supportFragmentManager.findFragmentByTag(savedInstanceState?.getString(KEY_FRAGMENT_TAG))?.let { fragment ->
            activeFragment = fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_FRAGMENT_TAG, activeFragment.customTag())
    }

    private fun observeSetWallpaperInProgress() {
        App.instance.setWallpaperStateLiveData.observe(this, Observer { state ->
            when (state) {
                SetWallpaperState.DOWNLOADING_PHOTO -> {
                    mainSetWallpaperInProgressLayout.visibility = View.VISIBLE
                    mainSetWallpaperDownloadingLayout.visibility = View.VISIBLE
                    mainSetWallpaperStatusLayout.visibility = View.GONE
                    mainSetWallpaperButton.visibility = View.GONE
                }

                SetWallpaperState.PHOTO_READY -> {
                    mainSetWallpaperInProgressLayout.visibility = View.VISIBLE
                    mainSetWallpaperDownloadingLayout.visibility = View.GONE
                    mainSetWallpaperStatusLayout.visibility = View.VISIBLE
                    mainSetWallpaperButton.visibility = View.VISIBLE

                    mainSetWallpaperStatusImage.setImageResource(R.drawable.ic_check_green_24dp)
                    mainSetWallpaperStatusText.setText(R.string.photo_ready)
                }

                SetWallpaperState.CANCEL -> {
                    mainSetWallpaperInProgressLayout.visibility = View.GONE
                }

                SetWallpaperState.ERROR_DOWNLOADING_PHOTO -> {
                    mainSetWallpaperInProgressLayout.visibility = View.VISIBLE
                    mainSetWallpaperDownloadingLayout.visibility = View.GONE
                    mainSetWallpaperStatusLayout.visibility = View.VISIBLE
                    mainSetWallpaperButton.visibility = View.GONE

                    mainSetWallpaperStatusImage.setImageResource(R.drawable.ic_close_red_24dp)
                    mainSetWallpaperStatusText.setText(R.string.error_downloading_photo)
                }

                else -> { }
            }
        })
    }

    private fun setupBottomNavigation(initialFragmentIndex: Int, initialItemId: Int) {
        activeFragment = fragments[initialFragmentIndex]

        mainBottomNavigation.selectedItemId = initialItemId

        fragments.forEachIndexed { index, fragment ->
            val transaction = supportFragmentManager.beginTransaction()
                    .add(R.id.mainNavigationContainer, fragment, fragment.customTag())

            if (index != initialFragmentIndex) {
                transaction.hide(fragment)
            }

            transaction.commit()
        }
    }

    private fun setNavigationItemSelectedListener() {
        mainBottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.photos -> {
                    changeFragment(fragments[PHOTOS_FRAGMENT_INDEX])
                    true
                }

                R.id.queue -> {
                    changeFragment(fragments[QUEUE_FRAGMENT_INDEX])
                    true
                }

                R.id.search -> {
                    changeFragment(fragments[SEARCH_FRAGMENT_INDEX])
                    true
                }

                R.id.favorites -> {
                    changeFragment(fragments[FAVORITES_FRAGMENT_INDEX])
                    true
                }

                R.id.more -> {
                    changeFragment(fragments[MORE_FRAGMENT_INDEX])
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .hide(findFragment(activeFragment))
                .show(findFragment(fragment))
                .commit()

        activeFragment = fragment

    }

    private fun findFragment(fragment: Fragment): Fragment {
        return supportFragmentManager.findFragmentByTag(fragment.customTag())!!
    }
}