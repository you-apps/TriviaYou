package com.bnyro.trivia.api.opentriviadb

import com.bnyro.trivia.extensions.formatStats
import com.bnyro.trivia.obj.Category
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.fasterxml.jackson.databind.ObjectMapper

object OpenTriviaDBHelper {
    private val mapper = ObjectMapper()

    suspend fun getQuestions(category: String?): List<Question> {
        val apiQuestions =
            RetrofitInstance.openTriviaApi.getQuestions(
                PreferenceHelper.getLimit(),
                category?.toInt(),
                PreferenceHelper.getDifficultyQuery()
            )
        val questions = mutableListOf<Question>()

        apiQuestions.results?.forEach {
            questions += Question(
                question = it.question,
                correctAnswer = it.correct_answer,
                incorrectAnswers = it.incorrect_answers
            )
        }
        return questions
    }

    suspend fun getCategories(): List<Category> {
        val categories = RetrofitInstance.openTriviaApi.getCategories()

        return categories.trivia_categories!!
    }

    suspend fun getStats(): List<String> {
        val metadata = RetrofitInstance.openTriviaApi.getStats()

        val stats = mutableListOf<String>()

        kotlin.runCatching {
            val json = mapper.readTree(
                mapper.writeValueAsString(metadata.overall)
            )
            json.fields().forEach {
                stats += it.formatStats()
            }
            return stats
        }
        return listOf()
    }
}
