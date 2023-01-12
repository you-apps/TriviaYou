package com.bnyro.trivia.api.opentriviadb.obj

import com.bnyro.trivia.obj.Category
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryResponse(
    val trivia_categories: List<Category>? = null
)
