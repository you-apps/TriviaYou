package com.bnyro.trivia.api.opentriviadb

import com.bnyro.trivia.obj.Category

data class OpenTriviaDBCategoryResponse(
    val trivia_categories: List<Category>? = null
)
