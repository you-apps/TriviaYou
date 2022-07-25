package com.bnyro.trivia

import android.app.Application
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.ThemeHelper
import com.google.android.material.color.DynamicColors

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // save context to the preference helper
        PreferenceHelper.setContext(this)

        // set dark or light theme
        ThemeHelper.setThemeMode(this)

        // apply material you colors
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
