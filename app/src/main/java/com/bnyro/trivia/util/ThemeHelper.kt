package com.bnyro.trivia.util

import android.content.Context
import android.content.res.Resources.Theme
import android.util.TypedValue

object ThemeHelper {
    fun getThemeColor(context: Context, colorCode: Int): Int {
        val typedValue = TypedValue()
        val theme: Theme = context.theme
        theme.resolveAttribute(colorCode, typedValue, true)
        return typedValue.data
    }
}
