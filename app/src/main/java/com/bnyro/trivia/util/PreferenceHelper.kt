package com.bnyro.trivia.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.obj.Quiz
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

object PreferenceHelper {
    private lateinit var context: Context
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val mapper = ObjectMapper()

    fun setContext(context: Context) {
        this.context = context
        settings = PreferenceManager.getDefaultSharedPreferences(context)
        editor = settings.edit()
    }

    fun getString(key: String, defaultValue: String): String {
        return settings.getString(key, defaultValue)!!
    }

    fun saveQuiz(name: String, questions: List<Question>) {
        val quizzes = getQuizzes().toMutableList()
        quizzes += Quiz(name, questions)

        val json = mapper.writeValueAsString(quizzes)
        editor.putString("quizzes", json)
    }

    fun getQuizzes(): List<Quiz> {
        val json = settings.getString("quizzes", "")
        val type = object : TypeReference<List<Quiz>>() {}
        return try {
            mapper.readValue(json, type)
        } catch (e: Exception) {
            listOf()
        }
    }
}
