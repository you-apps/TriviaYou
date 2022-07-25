package com.bnyro.trivia.util

import android.content.Context
import android.content.res.Resources.Theme
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate
import com.bnyro.trivia.R

object ThemeHelper {
    fun setThemeMode(context: Context) {
        val themeMode = when (
            PreferenceHelper.getString(
                context.getString(R.string.theme_mode_key),
                context.getString(R.string.theme_mode_default)
            )
        ) {
            "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun getThemeColor(context: Context, colorCode: Int): Int {
        val typedValue = TypedValue()
        val theme: Theme = context.theme
        theme.resolveAttribute(colorCode, typedValue, true)
        return typedValue.data
    }
}
