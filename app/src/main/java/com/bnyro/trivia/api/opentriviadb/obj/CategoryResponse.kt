package com.bnyro.trivia.api.opentriviadb.obj

import com.bnyro.trivia.obj.Category

data class CategoryResponse(
    val trivia_categories: List<Category>? = null
)
