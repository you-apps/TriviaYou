package com.bnyro.trivia.api.thetriviaapi.obj

data class ApiStats(
    val byCategory: Any? = null,
    val byDifficulty: Any? = null,
    val byState: Any? = null,
    val lastCreated: String? = null,
    val lastReviewed: String? = null
)
