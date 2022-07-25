package com.bnyro.trivia.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bnyro.trivia.R
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val resetStats = findPreference<Preference>(context?.getString(R.string.reset_stats_key)!!)
        resetStats?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.reset_stats)
                .setMessage(R.string.irreversible)
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    PreferenceHelper.resetTotalStats()
                }
                .show()
            true
        }

        val clearLibrary = findPreference<Preference>(context?.getString(R.string.clear_library_key)!!)
        clearLibrary?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.clear_library)
                .setMessage(R.string.irreversible)
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    PreferenceHelper.deleteAllQuizzes()
                }
                .show()
            true
        }
    }
}
