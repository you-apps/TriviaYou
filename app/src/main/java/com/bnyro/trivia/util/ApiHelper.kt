package com.bnyro.trivia.util

import com.bnyro.trivia.obj.Category
import com.bnyro.trivia.obj.Question

/**
 * API Scrapper
 */
abstract class ApiHelper {
    abstract suspend fun getQuestions(category: String?): List<Question>

    abstract suspend fun getCategories(): List<Category>

    abstract suspend fun getStats(): List<String>
}
