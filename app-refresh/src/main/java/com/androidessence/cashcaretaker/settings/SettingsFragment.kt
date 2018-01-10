package com.androidessence.cashcaretaker.settings

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceCategory
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.R
import timber.log.Timber

/**
 * Fragment for displaying a list of potential settings to the user.
 */
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private val fingerprintPreference: Preference by lazy { findPreference(getString(R.string.fingerprint_preference_key)) }
    private val securityCategory: PreferenceCategory by lazy { findPreference(getString(R.string.security_preferences_key)) as PreferenceCategory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        context?.let {
            view?.setBackgroundColor(ContextCompat.getColor(it, R.color.mds_white))
        }

        return view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        fingerprintPreference.onPreferenceChangeListener = this
//        findPreference(getString(R.string.pin_preference_key)).onPreferenceChangeListener = this

        // Hide if not supported
        context?.let {
            val manager = FingerprintManagerCompat.from(it)
            if (!manager.isHardwareDetected || !manager.hasEnrolledFingerprints()) {
                Timber.d("Removing preference.")
                securityCategory.removePreference(fingerprintPreference)
            }
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        Timber.d("Preference changed: ${preference?.key} - $newValue")

        return when (preference?.key) {
            getString(R.string.fingerprint_preference_key) -> {
                //TODO: Handle this being clicked.
                true
            }
            getString(R.string.pin_preference_key) -> {
                //TODO: Handle this being clicked.
                true
            }
            else -> false
        }
    }

    companion object {
        val FRAGMENT_NAME: String = SettingsFragment::class.java.simpleName

        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}