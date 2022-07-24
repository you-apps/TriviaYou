package com.bnyro.trivia.util

import com.bnyro.trivia.obj.Question
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface TriviaApi {
    @GET("api/questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int,
        @Query("categories") categories: String?
    ): ArrayList<Question>

    @GET("api/categories")
    suspend fun getCategories(): String
}
