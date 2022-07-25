package com.bnyro.trivia.api

import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.bnyro.trivia.util.capitalized
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper

object TheTriviaApiHelper {
    private val mapper = ObjectMapper()

    suspend fun getQuestions(category: String?): List<Question> {
        val theTriviaApiQuestions =
            RetrofitInstance.theTriviaApi.getQuestions(
                PreferenceHelper.getLimit(),
                category,
                PreferenceHelper.getDifficultyQuery()
            )
        val questions = mutableListOf<Question>()

        theTriviaApiQuestions.forEach {
            questions += Question(
                question = it.question,
                correctAnswer = it.correctAnswer,
                incorrectAnswers = it.incorrectAnswers
            )
        }
        return questions
    }

    suspend fun getCategories(): Pair<List<String>, List<String>> {
        val categories = RetrofitInstance.theTriviaApi.getCategories()
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)

        kotlin.runCatching {
            val answer = mapper.readTree(
                mapper.writeValueAsString(categories)
            )
            val categoryNames = mutableListOf<String>()
            val categoryQueries = mutableListOf<String>()
            answer.fieldNames().forEach {
                categoryNames += it.toString()
            }
            answer.elements().forEach {
                categoryQueries += it[0].toString()
            }
            return Pair(categoryNames, categoryQueries)
        }
        return Pair(listOf(), listOf())
    }

    suspend fun getStats(): List<String> {
        val metadata = RetrofitInstance.theTriviaApi.getMetadata()

        val stats = mutableListOf<String>()
        val mapper = ObjectMapper()

        kotlin.runCatching {
            val stateJson = mapper.writeValueAsString(metadata.byState)
            val stateStats = mapper.readTree(stateJson)

            stateStats.fields().forEach { field ->
                stats += field.toString().replace("=", ": ").capitalized()
            }

            val categoryJson = mapper.writeValueAsString(metadata.byCategory)
            val categoryStats = mapper.readTree(categoryJson)

            categoryStats.fields().forEach {
                stats += it.toString().replace("=", ": ").capitalized()
            }

            val difficultyJson = mapper.writeValueAsString(metadata.byDifficulty)
            val difficultyStats = mapper.readTree(difficultyJson)

            difficultyStats.fields().forEach { field ->
                stats += field.toString().replace("=", ": ").capitalized()
            }
            return stats
        }
        return listOf()
    }
}
