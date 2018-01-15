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
 *
 * @property[fingerprintPreference] The preference managing whether or not the user has fingerprint authentication for the app.
 * @property[pinPreference] The preference managing whether or not the user has pin authentication for the app.
 * @property[securityCategory] The category that security preferences sit in - this is used so we can hide the fingerprint option on certain devices.
 */
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private val fingerprintPreference: Preference by lazy { findPreference(getString(R.string.fingerprint_preference_key)) }
    private val pinPreference: Preference by lazy { findPreference(getString(R.string.pin_preference_key)) }
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
        pinPreference.onPreferenceChangeListener = this

        // Hide if not supported
        context?.let {
            val manager = FingerprintManagerCompat.from(it)
            if (!manager.isHardwareDetected || !manager.hasEnrolledFingerprints()) {
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
                // Pop up dialog asking for pin.
                true
            }
            else -> false
        }
    }

    companion object {
        /**
         * The tag for this fragment that is used to identify it in the backstack.
         */
        val FRAGMENT_NAME: String = SettingsFragment::class.java.simpleName

        /**
         * Creates a separate instance of this fragment to be displayed.
         */
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}