package com.bnyro.trivia.obj

data class Quiz(
    var name: String? = null,
    var creator: Boolean? = null,
    var questions: List<Question>? = null
)
