package com.androidessence.cashcaretaker.settings

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.androidessence.cashcaretaker.R
import timber.log.Timber

/**
 * Fragment for displaying a list of potential settings to the user.
 */
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        findPreference(getString(R.string.fingerprint_preference_key)).onPreferenceChangeListener = this
        findPreference(getString(R.string.pin_preference_key)).onPreferenceChangeListener = this
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