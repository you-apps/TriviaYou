package com.bnyro.trivia.util

import com.bnyro.trivia.obj.Category
import com.bnyro.trivia.obj.Question

/**
 * API Scrapper
 */
open class ApiHelper {
    open suspend fun getQuestions(category: String?): List<Question> {
        return listOf()
    }

    open suspend fun getCategories(): List<Category> {
        return listOf()
    }

    open suspend fun getStats(): List<String> {
        return listOf()
    }
}
