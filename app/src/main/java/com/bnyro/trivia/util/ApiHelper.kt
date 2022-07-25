package com.bnyro.trivia.util

import com.bnyro.trivia.api.opentriviadb.OpenTriviaDBHelper
import com.bnyro.trivia.api.thetriviaapi.TheTriviaApiHelper
import com.bnyro.trivia.obj.ApiType
import com.bnyro.trivia.obj.Question

class ApiHelper {
    private val apiPref = PreferenceHelper.getApi()

    suspend fun getQuestions(category: String?): List<Question> {
        return when (apiPref) {
            ApiType.theTriviaApi -> TheTriviaApiHelper.getQuestions(category)
            ApiType.openTriviaApi -> OpenTriviaDBHelper.getQuestions(category)
            else -> listOf()
        }
    }

    suspend fun getCategories(): Pair<List<String>, List<String>> {
        return when (apiPref) {
            ApiType.theTriviaApi -> TheTriviaApiHelper.getCategories()
            ApiType.openTriviaApi -> OpenTriviaDBHelper.getCategories()
            else -> Pair(listOf(), listOf())
        }
    }

    suspend fun getStats(): List<String> {
        return when (apiPref) {
            ApiType.theTriviaApi -> TheTriviaApiHelper.getStats()
            ApiType.openTriviaApi -> OpenTriviaDBHelper.getStats()
            else -> listOf()
        }
    }
}
