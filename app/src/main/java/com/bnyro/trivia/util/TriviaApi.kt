package com.bnyro.trivia.util

import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.obj.Stats
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface TriviaApi {
    @GET("api/questions")
    suspend fun getQuestions(
        @Query("limit") limit: Int,
        @Query("categories") categories: String?,
        @Query("difficulty") difficulty: String?
    ): ArrayList<Question>

    @GET("api/categories")
    suspend fun getCategories(): Any

    @GET("api/metadata")
    suspend fun getMetadata(): Stats
}
