package com.bnyro.trivia.api.opentriviadb

data class OpenTriviaDBQuestion(
    val category: String? = null,
    val correct_answer: String? = null,
    val difficulty: String? = null,
    val incorrect_answers: List<String>? = null,
    val question: String? = null,
    val type: String? = null
)
