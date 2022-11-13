package com.bnyro.trivia.api.opentriviadb

import com.bnyro.trivia.extensions.formatStats
import com.bnyro.trivia.obj.Category
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.ApiHelper
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.fasterxml.jackson.databind.ObjectMapper

class OpenTriviaDBHelper : ApiHelper() {
    private val mapper = ObjectMapper()

    override suspend fun getQuestions(category: String?): List<Question> {
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

    override suspend fun getCategories(): List<Category> {
        val categories = RetrofitInstance.openTriviaApi.getCategories()

        return categories.trivia_categories!!
    }

    override suspend fun getStats(): List<String> {
        val metadata = RetrofitInstance.openTriviaApi.getStats()

        val stats = mutableListOf<String>()

        runCatching {
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
