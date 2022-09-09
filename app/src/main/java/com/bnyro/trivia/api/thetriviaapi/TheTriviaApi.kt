package com.bnyro.trivia.api.thetriviaapi

import com.bnyro.trivia.api.thetriviaapi.obj.ApiStats
import com.bnyro.trivia.api.thetriviaapi.obj.Question
import retrofit2.http.GET
import retrofit2.http.Query

interface TheTriviaApi {
    @GET("api/questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int,
        @Query("categories") categories: String?,
        @Query("difficulty") difficulty: String?
    ): ArrayList<Question>

    @GET("api/categories")
    suspend fun getCategories(): Any

    @GET("api/metadata")
    suspend fun getStats(): ApiStats
}
