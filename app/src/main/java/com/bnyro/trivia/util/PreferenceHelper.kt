package com.bnyro.trivia.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHelper {
    private lateinit var context: Context
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun setContext(context: Context) {
        this.context = context
        settings = PreferenceManager.getDefaultSharedPreferences(context)
        editor = settings.edit()
    }

    fun getString(key: String, defaultValue: String): String {
        return settings.getString(key, defaultValue)!!
    }
}
