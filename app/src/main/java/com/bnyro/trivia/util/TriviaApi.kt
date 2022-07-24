package com.bnyro.trivia.util

import com.bnyro.trivia.obj.Question
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {
    @GET("api/questions")
    suspend fun getQuestions(@Query("limit") limit: Int): ArrayList<Question>
}
