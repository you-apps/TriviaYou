package com.bnyro.trivia

import android.app.Application
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.color.DynamicColors

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // save context to the preference helper
        PreferenceHelper.setContext(this)

        // apply material you colors
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
