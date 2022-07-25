package com.bnyro.trivia.api.opentriviadb

data class OpenTriviaDBQuestionResponse(
    val response_code: Int? = null,
    val results: List<OpenTriviaDBQuestion>? = null
)
