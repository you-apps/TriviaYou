package com.bnyro.trivia.api.opentriviadb

import com.bnyro.trivia.api.opentriviadb.obj.CategoryResponse
import com.bnyro.trivia.api.opentriviadb.obj.QuestionResponse
import com.bnyro.trivia.api.opentriviadb.obj.StatsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTriviaDB {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") limit: Int,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?
    ): QuestionResponse

    @GET("/api_category.php")
    suspend fun getCategories(): CategoryResponse

    @GET("api_count_global.php")
    suspend fun getStats(): StatsResponse
}
