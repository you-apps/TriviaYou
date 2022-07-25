package com.bnyro.trivia.api

import com.bnyro.trivia.obj.TheTriviaApiQuestion
import com.bnyro.trivia.obj.TheTriviaApiStats
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface OpenTriviaApi {
    @GET("api/questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int,
        @Query("categories") categories: String?,
        @Query("difficulty") difficulty: String?
    ): ArrayList<TheTriviaApiQuestion>

    @GET("api/categories")
    suspend fun getCategories(): Any

    @GET("api/metadata")
    suspend fun getMetadata(): TheTriviaApiStats
}
