package com.bnyro.trivia.api.thetriviaapi

import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface TheTriviaApi {
    @GET("api/questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int,
        @Query("categories") categories: String?,
        @Query("difficulty") difficulty: String?
    ): ArrayList<TheTriviaApiQuestion>

    @GET("api/categories")
    suspend fun getCategories(): Any

    @GET("api/metadata")
    suspend fun getStats(): TheTriviaApiStats
}
