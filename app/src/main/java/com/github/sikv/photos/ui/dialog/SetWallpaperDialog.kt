package com.github.sikv.photos.ui.dialog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.sikv.photos.R
import com.github.sikv.photos.enumeration.SetWallpaperState
import com.github.sikv.photos.model.Photo
import com.github.sikv.photos.util.Utils
import com.github.sikv.photos.viewmodel.SetWallpaperViewModel
import com.github.sikv.photos.viewmodel.SetWallpaperViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_set_wallpaper.*

class SetWallpaperDialog : BottomSheetDialogFragment() {

    companion object {
        private const val KEY_PHOTO = "photo"

        fun newInstance(photo: Photo): SetWallpaperDialog {
            val dialogFragment = SetWallpaperDialog()

            val args = Bundle()
            args.putParcelable(KEY_PHOTO, photo)

            dialogFragment.arguments = args

            return dialogFragment
        }
    }

    private val viewModel: SetWallpaperViewModel? by lazy {
        arguments?.getParcelable<Photo>(KEY_PHOTO)?.let { photo ->
            val viewModelFactory = SetWallpaperViewModelFactory(requireActivity().application, photo)

            ViewModelProvider(this, viewModelFactory).get(SetWallpaperViewModel::class.java)
        } ?: run {
            null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_bottom_sheet, container, false)

        val rootLayout = view.findViewById<ViewGroup>(R.id.rootLayout)

        val layout = LayoutInflater.from(context).inflate(R.layout.layout_set_wallpaper, rootLayout, false)

        rootLayout.addView(layout)

        Utils.addCancelOption(context, rootLayout, View.OnClickListener { dismiss() })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tryAgainButton.setOnClickListener {
            viewModel?.setWallpaper()
        }

        viewModel?.stateEvent?.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { stateWithData ->
                when (stateWithData.state) {
                    SetWallpaperState.DOWNLOADING_PHOTO -> {
                        statusText.setText(R.string.downloading_photo)

                        progressBar.visibility = View.VISIBLE
                        tryAgainButton.visibility = View.GONE
                    }

                    SetWallpaperState.PHOTO_READY -> {
                        statusText.setText(R.string.setting_wallpaper)

                        progressBar.visibility = View.VISIBLE
                        tryAgainButton.visibility = View.GONE

                        (stateWithData.data as? Uri)?.let { uri ->
                            viewModel?.setWallpaperFromUri(uri)
                            dismiss()
                        }
                    }

                    SetWallpaperState.ERROR_DOWNLOADING_PHOTO -> {
                        statusText.setText(R.string.error_downloading_photo)

                        progressBar.visibility = View.GONE
                        tryAgainButton.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "")
    }
}