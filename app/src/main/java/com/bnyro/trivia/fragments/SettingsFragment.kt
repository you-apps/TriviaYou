package com.bnyro.trivia.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.bnyro.trivia.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
