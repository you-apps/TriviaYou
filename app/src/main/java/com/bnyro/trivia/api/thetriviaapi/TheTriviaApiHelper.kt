package com.bnyro.trivia.api.thetriviaapi

import com.bnyro.trivia.extensions.formatStats
import com.bnyro.trivia.obj.Category
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.fasterxml.jackson.databind.ObjectMapper

object TheTriviaApiHelper {
    private val mapper = ObjectMapper()

    suspend fun getQuestions(category: String?): List<Question> {
        val apiQuestions =
            RetrofitInstance.theTriviaApi.getQuestions(
                PreferenceHelper.getLimit(),
                category,
                PreferenceHelper.getDifficultyQuery()
            )
        val questions = mutableListOf<Question>()

        apiQuestions.forEach {
            questions += Question(
                question = it.question,
                correctAnswer = it.correctAnswer,
                incorrectAnswers = it.incorrectAnswers
            )
        }
        return questions
    }

    suspend fun getCategories(): List<Category> {
        val categories = RetrofitInstance.theTriviaApi.getCategories()

        kotlin.runCatching {
            val response = mapper.readTree(
                mapper.writeValueAsString(categories)
            )
            val categoriesList = mutableListOf<Category>()
            response.fields().forEach {
                categoriesList += Category(
                    id = it.value[0].toString(),
                    name = it.key.toString()
                )
            }
            return categoriesList
        }
        return listOf()
    }

    suspend fun getStats(): List<String> {
        val metadata = RetrofitInstance.theTriviaApi.getStats()

        val stats = mutableListOf<String>()
        val mapper = ObjectMapper()

        kotlin.runCatching {
            val stateStats = mapper.readTree(
                mapper.writeValueAsString(metadata.byState)
            )

            stateStats.fields().forEach { field ->
                stats += field.formatStats()
            }

            val categoryStats = mapper.readTree(
                mapper.writeValueAsString(metadata.byCategory)
            )

            categoryStats.fields().forEach {
                stats += it.formatStats()
            }

            val difficultyStats = mapper.readTree(
                mapper.writeValueAsString(metadata.byDifficulty)
            )

            difficultyStats.fields().forEach { field ->
                stats += field.formatStats()
            }
            return stats
        }
        return listOf()
    }
}
