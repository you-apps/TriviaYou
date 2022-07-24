package com.bnyro.trivia.obj

data class Quiz(
    val name: String? = null,
    val isCreator: Boolean? = null,
    val questions: List<Question>? = null
)
