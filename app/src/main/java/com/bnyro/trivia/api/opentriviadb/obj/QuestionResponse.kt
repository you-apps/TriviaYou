package com.bnyro.trivia.api.opentriviadb.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class QuestionResponse(
    val response_code: Int? = null,
    val results: List<OpenTriviaQuestion>? = null
)
