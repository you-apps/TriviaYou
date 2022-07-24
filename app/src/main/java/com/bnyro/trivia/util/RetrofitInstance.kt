package com.bnyro.trivia.util

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {
    private const val url = "https://the-trivia-api.com/"

    val api: TriviaApi = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(TriviaApi::class.java)
}
