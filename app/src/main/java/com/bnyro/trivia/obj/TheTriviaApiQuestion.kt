package com.bnyro.trivia.obj

data class TheTriviaApiQuestion(
    val category: String? = null,
    val correctAnswer: String? = null,
    val difficulty: String? = null,
    val id: String? = null,
    val incorrectAnswers: List<String>? = null,
    val question: String? = null,
    val regions: List<Any>? = null,
    val tags: List<String>? = null,
    val type: String? = null
)
