package com.bnyro.trivia.util

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {
    private const val url = "https://the-trivia-api.com/"

    val theTriviaApi: TheTriviaApi = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(TheTriviaApi::class.java)
}
