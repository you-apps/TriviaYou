package com.bnyro.trivia.api.opentriviadb.obj

data class QuestionResponse(
    val response_code: Int? = null,
    val results: List<OpenTriviaQuestion>? = null
)
