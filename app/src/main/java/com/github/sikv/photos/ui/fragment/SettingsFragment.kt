package com.github.sikv.photos.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.sikv.photos.R
import com.github.sikv.photos.databinding.FragmentSettingsBinding
import com.github.sikv.photos.manager.ThemeManager
import com.github.sikv.photos.util.makeClickable
import com.github.sikv.photos.util.openUrl
import com.github.sikv.photos.util.setupToolbarWithBackButton
import com.github.sikv.photos.util.showFragment
import com.github.sikv.photos.viewmodel.SettingsViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    override val overrideBackground: Boolean = true

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PreferenceFragment())
            .commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarWithBackButton(
            title = R.string.settings,
            navigationOnClickListener = { navigation?.backPressed() }
        )

        showIconsAttribution()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun showIconsAttribution() {
        val thoseIcons = getString(R.string.those_icons)
        val thoseIconsUrl = "https://www.flaticon.com/authors/those-icons"

        val freepik = getString(R.string.freepik)
        val freepikUrl = "https://www.flaticon.com/authors/freepik"

        val flaticon = getString(R.string.flaticon)
        val flaticonUrl = "https://www.flaticon.com"

        binding.iconsAttributionText.text = getString(
            R.string.icons_made_by_s_and_s_from_s,
            thoseIcons, freepik, flaticon
        )

        binding.iconsAttributionText.makeClickable(arrayOf(
            thoseIcons, freepik, flaticon
        ),
            arrayOf(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        activity?.openUrl(thoseIconsUrl)
                    }
                },
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        activity?.openUrl(freepikUrl)
                    }
                },
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        activity?.openUrl(flaticonUrl)
                    }
                }
            )
        )
    }

    @AndroidEntryPoint
    class PreferenceFragment : PreferenceFragmentCompat() {

        @Inject
        lateinit var themeManager: ThemeManager

        private val viewModel: SettingsViewModel by viewModels()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            findPreference<ListPreference>(getString(R.string._pref_theme))
                ?.setOnPreferenceChangeListener { _, newValue ->
                    themeManager.applyTheme(newValue as? String)
                    true
                }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            observeAppVersion()
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            return when (preference?.key) {
                getString(R.string._pref_send_feedback) -> {
                    showFragment(FeedbackFragment())
                    return true
                }
                getString(R.string._pref_open_source_licences) -> {
                    startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                    OssLicensesMenuActivity.setActivityTitle(
                        context?.getString(R.string.open_source_licences) ?: ""
                    )
                    return true
                }
                else -> super.onPreferenceTreeClick(preference)
            }
        }

        private fun observeAppVersion() {
            viewModel.showAppVersionEvent.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let { appVersion ->
                    findPreference<Preference>(getString(R.string._pref_app_version))?.summary =
                        appVersion
                }
            })
        }
    }
}
