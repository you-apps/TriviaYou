package com.bnyro.trivia.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.bnyro.trivia.R
import com.bnyro.trivia.obj.ApiType
import com.bnyro.trivia.obj.Quiz
import com.bnyro.trivia.obj.UserStats
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

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    fun saveQuiz(quiz: Quiz) {
        val quizzes = getQuizzes().toMutableList()
        quizzes += quiz

        val json = mapper.writeValueAsString(quizzes)
        editor.putString(context.getString(R.string.quizzes_key), json).commit()
    }

    fun replaceQuizByIndex(index: Int, quiz: Quiz) {
        val quizzes = getQuizzes().toMutableList()
        quizzes[index] = quiz

        val json = mapper.writeValueAsString(quizzes)
        editor.putString(context.getString(R.string.quizzes_key), json).commit()
    }

    fun getQuizzes(): List<Quiz> {
        val json = settings.getString(context.getString(R.string.quizzes_key), "")
        val type = object : TypeReference<List<Quiz>>() {}
        return try {
            mapper.readValue(json, type)
        } catch (e: Exception) {
            listOf()
        }
    }

    fun deleteQuiz(index: Int) {
        val quizzes = getQuizzes().toMutableList()
        quizzes.removeAt(index)
        val json = mapper.writeValueAsString(quizzes)
        editor.putString(
            context.getString(R.string.quizzes_key),
            json
        ).commit()
    }

    fun deleteAllQuizzes() {
        editor.putString(
            context.getString(R.string.quizzes_key),
            ""
        ).commit()
    }

    fun getDifficultyQuery(): String? {
        val difficultyPref = getString(
            context.getString(R.string.difficulty_key),
            context.getString(R.string.difficulty_default)
        )
        return when (difficultyPref) {
            "random" -> null
            else -> difficultyPref
        }
    }

    fun getLimit(): Int {
        return getString(
            context.getString(R.string.limit_key),
            context.getString(R.string.limit_default)
        ).toInt()
    }

    fun isUnlimitedMode(): Boolean {
        return getBoolean(context.getString(R.string.unlimited_mode_key), true)
    }

    fun getTotalStats(): UserStats {
        val json = settings.getString(context.getString(R.string.stats_key), "")
        val type = object : TypeReference<UserStats>() {}
        return try {
            mapper.readValue(json, type)
        } catch (e: Exception) {
            UserStats()
        }
    }

    fun addToTotalStats(
        totalQuestions: Int = 0,
        correctAnswers: Int = 0
    ) {
        val userStats = getTotalStats()
        userStats.totalAnswers += totalQuestions
        userStats.correctAnswers += correctAnswers
        val json = mapper.writeValueAsString(userStats)
        editor.putString(context.getString(R.string.stats_key), json).commit()
    }

    fun resetTotalStats() {
        editor.putString(context.getString(R.string.stats_key), "").commit()
    }

    fun getApi(): Int {
        val apiPref = getString(
            context.getString(R.string.api_key),
            context.getString(R.string.api_default)
        )

        return when (apiPref) {
            "TheTriviaAPI" -> ApiType.theTriviaApi
            "OpenTriviaDB" -> ApiType.openTriviaApi
            else -> ApiType.theTriviaApi
        }
    }

    fun setQuizPosition(quizIndex: Int, questionIndex: Int) {
        val quiz = getQuizzes()[quizIndex]
        quiz.position = questionIndex
        replaceQuizByIndex(quizIndex, quiz)
    }
}
