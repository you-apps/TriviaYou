package com.bnyro.trivia.api.opentriviadb

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTriviaDB {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") limit: Int,
        @Query("categories") category: String?,
        @Query("difficulty") difficulty: String?
    ): OpenTriviaDBQuestionResponse

    @GET("/api_category.php")
    suspend fun getCategories(): OpenTriviaDBCategoryResponse

    @GET("api_count_global.php")
    suspend fun getStats(): OpenTriviaDBStatsResponse
}
