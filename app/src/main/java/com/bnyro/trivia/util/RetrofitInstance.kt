package com.bnyro.trivia.util

import com.bnyro.trivia.TRIVIA_API_URL
import com.bnyro.trivia.api.OpenTriviaApi
import com.bnyro.trivia.api.TheTriviaApi
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {

    val theTriviaApi: TheTriviaApi = Retrofit.Builder()
        .baseUrl(TRIVIA_API_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(TheTriviaApi::class.java)

    val openTriviaApi: OpenTriviaApi = Retrofit.Builder()
        .baseUrl(TRIVIA_API_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(OpenTriviaApi::class.java)
}
