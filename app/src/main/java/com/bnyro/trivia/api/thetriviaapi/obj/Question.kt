package com.bnyro.trivia.api.thetriviaapi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Question(
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
