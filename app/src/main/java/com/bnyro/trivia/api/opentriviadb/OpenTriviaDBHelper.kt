package com.bnyro.trivia.api.opentriviadb

import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.bnyro.trivia.util.formatStats
import com.fasterxml.jackson.databind.ObjectMapper

object OpenTriviaDBHelper {
    private val mapper = ObjectMapper()

    suspend fun getQuestions(category: String?): List<Question> {
        val apiQuestions =
            RetrofitInstance.openTriviaApi.getQuestions(
                PreferenceHelper.getLimit(),
                category,
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

    suspend fun getCategories(): Pair<List<String>, List<String>> {
        val categories = RetrofitInstance.openTriviaApi.getCategories()

        kotlin.runCatching {
            val categoryNames = mutableListOf<String>()
            val categoryQueries = mutableListOf<String>()
            categories.trivia_categories?.forEach {
                categoryNames += it.name!!
                categoryQueries += it.id.toString()
            }
            return Pair(categoryNames, categoryQueries)
        }
        return Pair(listOf(), listOf())
    }

    suspend fun getStats(): List<String> {
        val metadata = RetrofitInstance.openTriviaApi.getStats()

        val stats = mutableListOf<String>()
        val mapper = ObjectMapper()

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
